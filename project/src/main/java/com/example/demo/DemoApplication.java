package com.example.demo;

import com.example.demo.config.LambdaHandler;
import com.example.demo.config.MyRuntimeHints;
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
    public static void main(String[] args) throws IOException {
        LambdaHandler handler = new LambdaHandler();
        InputStream inputStream = new ByteArrayInputStream(args[0].getBytes());
        OutputStream outputStream = new FileOutputStream("temp1.txt");
        handler.handleRequest(inputStream, outputStream, null);
    }
}
