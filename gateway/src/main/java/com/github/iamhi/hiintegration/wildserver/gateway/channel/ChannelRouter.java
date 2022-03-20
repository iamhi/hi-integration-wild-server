package com.github.iamhi.hiintegration.wildserver.gateway.channel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ChannelRouter {

    private static final String ROUTER_PREFIX = "/channel";

    @Bean
    public RouterFunction<ServerResponse> channelRouterCompose(ChannelHandler channelHandler) {
        return route(POST(ROUTER_PREFIX), channelHandler::joinChannel);
    }
}
