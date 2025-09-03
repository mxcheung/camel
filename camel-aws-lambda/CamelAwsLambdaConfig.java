package com.example.camel.config;

import org.apache.camel.component.aws2.lambda.AWS2LambdaComponent;
import org.apache.camel.CamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelAwsLambdaConfig {

    /**
     * Registers the AWS2 Lambda component with CamelContext
     * so that "aws2-lambda://functionName" endpoints can be used in routes.
     */
    @Bean
    public AWS2LambdaComponent aws2LambdaComponent(CamelContext camelContext) {
        AWS2LambdaComponent lambdaComponent = new AWS2LambdaComponent();
        lambdaComponent.setCamelContext(camelContext);

        // Optional: if you want to use a custom AWS client or region
        // lambdaC
