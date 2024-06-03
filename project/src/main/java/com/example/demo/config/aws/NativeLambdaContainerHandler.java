package com.example.demo.config.aws;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.ExceptionHandler;
import com.amazonaws.serverless.proxy.InitializationWrapper;
import com.amazonaws.serverless.proxy.RequestReader;
import com.amazonaws.serverless.proxy.ResponseWriter;
import com.amazonaws.serverless.proxy.SecurityContextWriter;
import com.amazonaws.serverless.proxy.internal.servlet.AwsHttpServletRequest;
import com.amazonaws.serverless.proxy.internal.servlet.AwsHttpServletResponse;
import com.amazonaws.serverless.proxy.internal.servlet.AwsLambdaServletContainerHandler;
import com.amazonaws.serverless.proxy.internal.servlet.AwsServletContext;
import com.amazonaws.serverless.proxy.internal.servlet.AwsServletRegistration;
import com.amazonaws.serverless.proxy.internal.testutils.Timer;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.example.demo.config.aws.embedded.NativeServerlessEmbeddedServerFactory;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.CountDownLatch;

public class NativeLambdaContainerHandler extends AwsLambdaServletContainerHandler<AwsProxyRequest, AwsProxyResponse, HttpServletRequest, AwsHttpServletResponse> {
    private static final String DISPATCHER_SERVLET_REGISTRATION_NAME = "dispatcherServlet";

    private final Class<?> springBootInitializer;
    private static final Logger log = LoggerFactory.getLogger(NativeLambdaContainerHandler.class);
    private String[] springProfiles = null;
    private WebApplicationType springWebApplicationType;
    private ConfigurableApplicationContext applicationContext;

    private static NativeLambdaContainerHandler instance;

    private boolean initialized;

    public static NativeLambdaContainerHandler getInstance() {
        return instance;
    }

    public NativeLambdaContainerHandler(
            RequestReader<AwsProxyRequest, HttpServletRequest> requestReader,
            ResponseWriter<AwsHttpServletResponse, AwsProxyResponse> responseWriter,
            SecurityContextWriter<AwsProxyRequest> securityContextWriter,
            ExceptionHandler<AwsProxyResponse> exceptionHandler,
            Class<?> springBootInitializer,
            InitializationWrapper init,
            WebApplicationType applicationType
    ) {
        super(AwsProxyRequest.class, AwsProxyResponse.class, requestReader, responseWriter, securityContextWriter, exceptionHandler);
        Timer.start("SPRINGBOOT2_CONTAINER_HANDLER_CONSTRUCTOR");
        initialized = false;
        this.springBootInitializer = springBootInitializer;
        springWebApplicationType = applicationType;
        setInitializationWrapper(init);
        NativeLambdaContainerHandler.setInstance(this);

        Timer.stop("SPRINGBOOT2_CONTAINER_HANDLER_CONSTRUCTOR");
    }

    private static void setInstance(NativeLambdaContainerHandler h) {
        NativeLambdaContainerHandler.instance = h;
    }

    public void activateSpringProfiles(String... profiles) {
        springProfiles = profiles;
        // force a re-initialization
        initialized = false;
    }

    @Override
    protected AwsHttpServletResponse getContainerResponse(HttpServletRequest request, CountDownLatch latch) {
        return new AwsHttpServletResponse(request, latch);
    }

    @Override
    protected void handleRequest(HttpServletRequest containerRequest, AwsHttpServletResponse containerResponse, Context lambdaContext) throws Exception {
        // this method of the AwsLambdaServletContainerHandler sets the servlet context
        Timer.start("SPRINGBOOT2_HANDLE_REQUEST");

        // wire up the application context on the first invocation
        if (!initialized) {
            initialize();
        }

        // process filters & invoke servlet
        Servlet reqServlet = ((AwsServletContext) getServletContext()).getServletForPath(containerRequest.getPathInfo());
        if (AwsHttpServletRequest.class.isAssignableFrom(containerRequest.getClass())) {
            ((AwsHttpServletRequest) containerRequest).setServletContext(getServletContext());
            ((AwsHttpServletRequest) containerRequest).setResponse(containerResponse);
        }
        doFilter(containerRequest, containerResponse, reqServlet);
        Timer.stop("SPRINGBOOT2_HANDLE_REQUEST");
    }


    @Override
    public void initialize()
            throws ContainerInitializationException {
        Timer.start("SPRINGBOOT2_COLD_START");

        SpringApplicationBuilder builder = new SpringApplicationBuilder(getEmbeddedContainerClasses())
                .web(springWebApplicationType); // .REACTIVE, .SERVLET
        if (springProfiles != null) {
            builder.profiles(springProfiles);
        }
        applicationContext = builder.run();
        if (springWebApplicationType == WebApplicationType.SERVLET) {
            ((ServletWebServerApplicationContext) applicationContext).setServletContext(getServletContext());
            AwsServletRegistration reg = (AwsServletRegistration) getServletContext().getServletRegistration(DISPATCHER_SERVLET_REGISTRATION_NAME);
            if (reg != null) {
                reg.setLoadOnStartup(1);
            }
        }
        super.initialize();
        initialized = true;
        Timer.stop("SPRINGBOOT2_COLD_START");
    }

    private Class<?>[] getEmbeddedContainerClasses() {
        Class<?>[] classes = new Class[2];
        classes[0] = NativeServerlessEmbeddedServerFactory.class;
        classes[1] = springBootInitializer;
        return classes;
    }
}

