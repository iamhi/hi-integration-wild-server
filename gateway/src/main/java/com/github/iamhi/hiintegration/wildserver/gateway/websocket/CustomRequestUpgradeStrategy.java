package com.github.iamhi.hiintegration.wildserver.gateway.websocket;

import com.github.iamhi.hiintegration.wildserver.core.TokenService;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.AbstractServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.adapter.NettyWebSocketSessionSupport;
import org.springframework.web.reactive.socket.adapter.ReactorNettyWebSocketSession;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

import java.util.function.Supplier;

public record CustomRequestUpgradeStrategy(
    TokenService tokenService
) implements RequestUpgradeStrategy {

    private static HttpServerResponse getNativeResponse(ServerHttpResponse response) {
        if (response instanceof AbstractServerHttpResponse abstractServerHttpResponse) {
            return abstractServerHttpResponse.getNativeResponse();
        } else if (response instanceof ServerHttpResponseDecorator serverHttpResponseDecorator) {
            return getNativeResponse(serverHttpResponseDecorator.getDelegate());
        } else {
            throw new IllegalArgumentException("Couldn't find native response in " + response.getClass()
                .getName());
        }
    }

    @Override
    public Mono<Void> upgrade(ServerWebExchange exchange, WebSocketHandler webSocketHandler, String subProtocol, Supplier<HandshakeInfo> handshakeInfoFactory) {
        ServerHttpResponse response = exchange.getResponse();
        HttpServerResponse reactorResponse = getNativeResponse(response);
        HandshakeInfo handshakeInfo = handshakeInfoFactory.get();
        NettyDataBufferFactory bufferFactory = (NettyDataBufferFactory) response.bufferFactory();

        return Mono.justOrEmpty(handshakeInfo.getHeaders().getFirst("connect-token"))
            .flatMap(tokenService::connectWithToken)
            .flatMap(token -> reactorResponse.sendWebsocket((in, out) -> {
                ReactorNettyWebSocketSession session = new ReactorNettyWebSocketSession(in, out, handshakeInfo, bufferFactory, NettyWebSocketSessionSupport.DEFAULT_FRAME_MAX_SIZE);

                session.getAttributes().put("token", token);

                return webSocketHandler.handle(session);
            }));
    }
}
