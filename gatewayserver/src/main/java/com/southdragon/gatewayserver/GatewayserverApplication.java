package com.southdragon.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator southdragonRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r
						.path("/southdragon/accounts/**")
						.filters(f -> f.rewritePath("southdragon/accounts/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://accounts"))
				.route(r -> r
						.path("/southdragon/loans/**")
						.filters(f -> f.rewritePath("southdragon/loans/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://loans"))
				.route(r -> r
						.path("/southdragon/cards/**")
						.filters(f -> f.rewritePath("southdragon/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://cards"))
				.build();
	}
}
