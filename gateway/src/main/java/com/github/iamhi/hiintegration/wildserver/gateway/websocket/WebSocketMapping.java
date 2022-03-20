package com.github.iamhi.hiintegration.wildserver.gateway.websocket;

import com.github.iamhi.hiintegration.wildserver.core.TokenService;
import com.github.iamhi.hiintegration.wildserver.gateway.websocket.handlers.ExtendedWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class WebSocketMapping {

    private static final String WEBSOCKET_PREFIX_ROUTE = "/ws";

    @Bean
    public HandlerMapping webSocketHandlerMapping(
        List<ExtendedWebSocketHandler> handlerList
    ) {
        return new SimpleUrlHandlerMapping(
            handlerList.stream().collect(Collectors.toMap(handler -> WEBSOCKET_PREFIX_ROUTE + handler.getPath(),
                handler -> handler)),
            1);
    }

    @Bean
    public HandlerAdapter handlerAdapter(@Autowired TokenService tokenService) {
        return new WebSocketHandlerAdapter(webSocketService(tokenService));
    }

    @Bean
    public WebSocketService webSocketService(TokenService tokenService) {
        return new HandshakeWebSocketService(new CustomRequestUpgradeStrategy(tokenService));
    }
}
