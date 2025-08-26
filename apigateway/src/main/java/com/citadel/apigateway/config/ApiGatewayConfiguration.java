package com.citadel.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("user-service", r -> r.path("/users/**")
								.filters(f -> f.stripPrefix(1))
						.uri("lb://userservice"))
				.route("product-service", r -> r.path("/products/**")
						.uri("lb://productservice"))
				.route("inventory-service", r -> r.path("/inventory/**")
						.uri("lb://inventoryservice"))
				.route("cart-service", r -> r.path("/cart/**")
						.uri("lb://cartservice"))
				.build();
	}
}
