package com.hrms.Human_Resource_Management_System_Back.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class EndpointLoggerConfig {

    private static final Logger logger = LoggerFactory.getLogger(EndpointLoggerConfig.class);

    @Bean
    public ApplicationListener<ApplicationReadyEvent> logEndpoints(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        return event -> {
            logger.info("============= API ENDPOINTS =============");

            Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
            List<String> endpoints = new ArrayList<>();

            handlerMethods.forEach((mappingInfo, handlerMethod) -> {
                var patternsCondition = mappingInfo.getPatternsCondition();
                if (patternsCondition != null && !patternsCondition.getPatterns().isEmpty()) {
                    for (String pattern : patternsCondition.getPatterns()) {
                        String controllerName = handlerMethod.getBeanType().getSimpleName();
                        String methodName = handlerMethod.getMethod().getName();
                        String httpMethods = mappingInfo.getMethodsCondition().getMethods().isEmpty()
                                ? "ALL"
                                : mappingInfo.getMethodsCondition().getMethods().stream()
                                .map(Enum::toString)
                                .collect(Collectors.joining(","));

                        endpoints.add(httpMethods + " " + pattern + " -> " + controllerName + "." + methodName + "()");
                    }
                }
            });

            endpoints.sort(String::compareTo);
            endpoints.forEach(logger::info);
            logger.info("========= END OF API ENDPOINTS =========");
        };
    }

}