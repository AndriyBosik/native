package com.example.demo.config.aws;

import com.amazonaws.serverless.proxy.internal.servlet.AwsHttpServletResponse;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestIdentity;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyRequestContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.Headers;
import com.amazonaws.serverless.proxy.model.MultiValuedTreeMap;
import com.amazonaws.serverless.proxy.model.SingleValueHeaders;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.aot.generate.GenerationContext;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
import org.springframework.beans.factory.aot.BeanFactoryInitializationCode;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class AwsSpringAotTypesProcessor implements BeanFactoryInitializationAotProcessor {
    @Override
    public BeanFactoryInitializationAotContribution processAheadOfTime(ConfigurableListableBeanFactory beanFactory) {
        return new ReflectiveProcessorBeanFactoryInitializationAotContribution();
    }

    private static final class ReflectiveProcessorBeanFactoryInitializationAotContribution implements BeanFactoryInitializationAotContribution {
        @Override
        public void applyTo(GenerationContext generationContext, BeanFactoryInitializationCode beanFactoryInitializationCode) {
            RuntimeHints runtimeHints = generationContext.getRuntimeHints();
            // known static types

            runtimeHints.reflection().registerType(AwsProxyRequest.class,
                    MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.DECLARED_FIELDS, MemberCategory.DECLARED_CLASSES);
            runtimeHints.reflection().registerType(AwsProxyResponse.class,
                    MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.DECLARED_FIELDS, MemberCategory.DECLARED_CLASSES);
            runtimeHints.reflection().registerType(SingleValueHeaders.class,
                    MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.DECLARED_FIELDS, MemberCategory.DECLARED_CLASSES);
            runtimeHints.reflection().registerType(JsonToken.class,
                    MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.DECLARED_FIELDS, MemberCategory.DECLARED_CLASSES);
            runtimeHints.reflection().registerType(MultiValuedTreeMap.class,
                    MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.DECLARED_FIELDS, MemberCategory.DECLARED_CLASSES);
            runtimeHints.reflection().registerType(Headers.class,
                    MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.DECLARED_FIELDS, MemberCategory.DECLARED_CLASSES);
            runtimeHints.reflection().registerType(AwsProxyRequestContext.class,
                    MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.DECLARED_FIELDS, MemberCategory.DECLARED_CLASSES);
            runtimeHints.reflection().registerType(ApiGatewayRequestIdentity.class,
                    MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.DECLARED_FIELDS, MemberCategory.DECLARED_CLASSES);
            runtimeHints.reflection().registerType(AwsHttpServletResponse.class,
                    MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.DECLARED_FIELDS, MemberCategory.DECLARED_CLASSES, MemberCategory.INTROSPECT_DECLARED_METHODS);
        }
    }
}
