package com.example.demo.config.aws;

import com.amazonaws.serverless.proxy.AwsProxyExceptionHandler;
import com.amazonaws.serverless.proxy.ExceptionHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import org.springframework.web.ErrorResponse;

public class SpringBootAwsProxyExceptionHandler extends AwsProxyExceptionHandler
        implements ExceptionHandler<AwsProxyResponse> {
    @Override
    public AwsProxyResponse handle(Throwable ex) {
        if (ex instanceof ErrorResponse) {
            return new AwsProxyResponse(((ErrorResponse) ex).getStatusCode().value(),
                    HEADERS, getErrorJson(ex.getMessage()));
        } else {
            return super.handle(ex);
        }
    }
}
