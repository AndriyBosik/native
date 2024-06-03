package com.example.demo.config.aws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

public class NativeWebRuntimeInitializer implements ApplicationContextInitializer<GenericApplicationContext> {
    private static Log logger = LogFactory.getLog(NativeWebRuntimeInitializer.class);

    @Override
    public void initialize(GenericApplicationContext context) {
        logger.info("AWS Environment: " + System.getenv());
        Environment environment = context.getEnvironment();
        if (logger.isDebugEnabled()) {
            logger.debug("AWS Environment: " + System.getenv());
        }

        if (context instanceof ServletWebServerApplicationContext && isCustomRuntime(environment)) {
            if (context.getBeanFactory().getBeanNamesForType(AwsSpringWebCustomRuntimeEventLoop.class, false, false).length == 0) {
                context.registerBean(StringUtils.uncapitalize(AwsSpringWebCustomRuntimeEventLoop.class.getSimpleName()),
                        SmartLifecycle.class, () -> new AwsSpringWebCustomRuntimeEventLoop((ServletWebServerApplicationContext) context));
            }
        }
    }

    private boolean isCustomRuntime(Environment environment) {
        String handler = environment.getProperty("_HANDLER");
        if (StringUtils.hasText(handler)) {
            handler = handler.split(":")[0];
            logger.info("AWS Handler: " + handler);
            try {
                Thread.currentThread().getContextClassLoader().loadClass(handler);
            } catch (Exception e) {
                logger.debug("Will execute Lambda in Custom Runtime");
                return true;
            }
        }
        return false;
    }
}
