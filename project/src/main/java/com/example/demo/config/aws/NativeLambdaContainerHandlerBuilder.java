package com.example.demo.config.aws;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.ExceptionHandler;
import com.amazonaws.serverless.proxy.internal.servlet.ServletLambdaContainerHandlerBuilder;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.WebApplicationType;

public final class NativeLambdaContainerHandlerBuilder extends ServletLambdaContainerHandlerBuilder<
        AwsProxyRequest,
        AwsProxyResponse,
        HttpServletRequest,
        NativeLambdaContainerHandler,
        NativeLambdaContainerHandlerBuilder> {
    private Class<?> springBootInitializer;
    private String[] profiles;
    private WebApplicationType applicationType = WebApplicationType.SERVLET;

    @Override
    protected NativeLambdaContainerHandlerBuilder self() {
        return this;
    }


    public NativeLambdaContainerHandlerBuilder springBootApplication(Class<?> app) {
        springBootInitializer = app;
        return self();
    }

    public NativeLambdaContainerHandlerBuilder profiles(String... profiles) {
        this.profiles = profiles;
        return self();
    }

    public NativeLambdaContainerHandlerBuilder servletApplication() {
        this.applicationType = WebApplicationType.SERVLET;
        return self();
    }

    @Override
    public NativeLambdaContainerHandler build() throws ContainerInitializationException {
        validate();
        if (springBootInitializer == null) {
            throw new ContainerInitializationException("Missing spring boot application class in builder", null);
        }
        NativeLambdaContainerHandler handler = new NativeLambdaContainerHandler(
                requestReader,
                responseWriter,
                securityContextWriter,
                exceptionHandler,
                springBootInitializer,
                initializationWrapper,
                applicationType
        );
        if (profiles != null) {
            handler.activateSpringProfiles(profiles);
        }
        return handler;
    }

    @Override
    public NativeLambdaContainerHandler buildAndInitialize() throws ContainerInitializationException {
        NativeLambdaContainerHandler handler = build();
        initializationWrapper.start(handler);
        return handler;
    }

    @Override
    protected ExceptionHandler<AwsProxyResponse> defaultExceptionHandler() {
        return new SpringBootAwsProxyExceptionHandler();
    }
}

