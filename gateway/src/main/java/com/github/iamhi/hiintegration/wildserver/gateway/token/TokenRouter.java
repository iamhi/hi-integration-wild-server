package com.github.iamhi.hiintegration.wildserver.gateway.token;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TokenRouter {

    private static final String ROUTER_PREFIX = "/token";

    @Bean
    public RouterFunction<ServerResponse> tokenRouterCompose(TokenHandler tokenHandler) {
        return route(GET(ROUTER_PREFIX), tokenHandler::createToken);
    }
}
