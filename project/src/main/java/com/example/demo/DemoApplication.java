package com.example.demo;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.example.demo.config.MyRuntimeHints;
import com.example.demo.config.aws.NativeLambdaContainerHandler;
import com.example.demo.config.aws.NativeLambdaContainerHandlerBuilder;
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
//        if (args.length == 0) {
//            SpringApplication.run(DemoApplication.class, args);
//            return;
//        }

//        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);

        System.out.println(System.getenv("AWS_LAMBDA_INITIALIZATION_TYPE"));
        NativeLambdaContainerHandler handler = new NativeLambdaContainerHandlerBuilder()
                .defaultProxy()
                .springBootApplication(DemoApplication.class)
                .servletApplication()
                .buildAndInitialize();
        if (args.length > 0) {
            InputStream inputStream = new ByteArrayInputStream(args[0].getBytes());
            OutputStream outputStream = new FileOutputStream("temp1.txt");
            handler.proxyStream(inputStream, outputStream, null);
        }
    }
}
