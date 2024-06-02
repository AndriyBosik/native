package com.example.demo;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
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

    public static void main(String[] args) throws IOException, ContainerInitializationException {
        SpringApplication.run(DemoApplication.class, args);
        SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler = SpringBootLambdaContainerHandler
                .getAwsProxyHandler(DemoApplication.class);
        if (args.length == 0) {
            return;
        }
        InputStream inputStream = new ByteArrayInputStream(args[0].getBytes());
        OutputStream outputStream = new FileOutputStream("temp1.txt");
        handler.proxyStream(inputStream, outputStream, null);
    }
}
