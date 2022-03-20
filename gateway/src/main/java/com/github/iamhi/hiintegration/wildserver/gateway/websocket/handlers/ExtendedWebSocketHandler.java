package com.github.iamhi.hiintegration.wildserver.gateway.websocket.handlers;

import org.springframework.web.reactive.socket.WebSocketHandler;

public interface ExtendedWebSocketHandler extends WebSocketHandler {

    String getPath();
}
