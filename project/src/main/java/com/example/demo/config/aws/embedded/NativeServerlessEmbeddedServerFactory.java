package com.example.demo.config.aws.embedded;

import com.amazonaws.serverless.proxy.internal.servlet.AwsLambdaServletContainerHandler;
import com.example.demo.config.aws.NativeLambdaContainerHandler;
import jakarta.servlet.ServletException;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.core.Ordered;

@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class NativeServerlessEmbeddedServerFactory implements ServletWebServerFactory, WebServer {
    @SuppressWarnings("rawtypes")
    private AwsLambdaServletContainerHandler handler;

    public NativeServerlessEmbeddedServerFactory() {
        super();
        handler = NativeLambdaContainerHandler.getInstance();
    }

    @Override
    public WebServer getWebServer(ServletContextInitializer... initializers) {
        for (ServletContextInitializer i : initializers) {
            try {
                if (handler.getServletContext() == null) {
                    throw new WebServerException("Attempting to initialize ServletEmbeddedWebServer without ServletContext in Handler", null);
                }
                i.onStartup(handler.getServletContext());
            } catch (ServletException e) {
                throw new WebServerException("Could not initialize Servlets", e);
            }
        }
        return this;
    }

    @Override
    public void start() throws WebServerException {

    }

    @Override
    public void stop() throws WebServerException {

    }

    @Override
    public int getPort() {
        return 0;
    }
}
