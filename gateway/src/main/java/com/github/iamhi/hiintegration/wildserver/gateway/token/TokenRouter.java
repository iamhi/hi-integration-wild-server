package com.github.iamhi.hiintegration.wildserver.gateway.token;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TokenRouter {

    private static final String ROUTER_PREFIX = "/token";

    @Bean
    public RouterFunction<ServerResponse> tokenRouterCompose(TokenHandler tokenHandler) {
        return route(POST(ROUTER_PREFIX), tokenHandler::createToken);
    }
}
