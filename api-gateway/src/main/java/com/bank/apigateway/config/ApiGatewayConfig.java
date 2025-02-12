//package com.bank.apigateway.config;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ApiGatewayConfig {
//
//    /**
//     * Captures {service-name} dynamically from the request.
//     * Removes /api-gateway/{service-name} before forwarding.
//     * Uses lb://SERVICE-NAME to route dynamically using Eureka.
//     * @param builder
//     * @return RouteLocator
//     */
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//
//        return builder.routes()
//                .route("dynamic-route", r -> r
//                        .path("/api-gateway/{service-name}/**")
//                        .filters(f -> f.rewritePath("/api-gateway/(?<service>[^/]+)/(.*)", "/${service}/$2"))
//                        .uri("lb://${service-name}"))  // Dynamically resolve service
//                .build();
//    }
//}
