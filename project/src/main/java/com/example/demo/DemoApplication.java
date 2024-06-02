package com.example.demo;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.example.demo.config.MyRuntimeHints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SpringBootApplication
@ImportRuntimeHints(MyRuntimeHints.class)
public class DemoApplication {
//    private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
//
//    static {
//        try {
//            handler = new SpringBootProxyHandlerBuilder<AwsProxyRequest>()
//                    .defaultProxy()
//                    .springBootApplication(DemoApplication.class)
//                    .buildAndInitialize();
//        } catch (
//                ContainerInitializationException exception) {
//            throw new RuntimeException("Could not initialize Spring Boot application", exception);
//        }
//    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(DemoApplication.class, args);
//        if (args.length == 0) {
//            return;
//        }
//        InputStream inputStream = new ByteArrayInputStream(args[0].getBytes());
//        OutputStream outputStream = new FileOutputStream("temp1.txt");
//        handler.proxyStream(inputStream, outputStream, null);
    }
}
