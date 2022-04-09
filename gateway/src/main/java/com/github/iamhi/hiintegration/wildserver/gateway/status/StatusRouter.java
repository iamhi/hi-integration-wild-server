package com.github.iamhi.hiintegration.wildserver.gateway.status;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class StatusRouter {

    private static final String ROUTER_PREFIX = "/status";

    @Bean
    public RouterFunction<ServerResponse> composeRoute() {
        return route(GET(ROUTER_PREFIX + "/ping"), serverRequest ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(Map.of("ping", "PONG from Connections")), Map.class));
    }
}
