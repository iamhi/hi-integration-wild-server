package com.github.iamhi.hiintegration.wildserver.gateway.websocket.handlers;

import com.github.iamhi.hiintegration.wildserver.core.MessageListenerService;
import com.github.iamhi.hiintegration.wildserver.core.MessageSupplierService;
import com.github.iamhi.hiintegration.wildserver.core.dto.UserInputDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Service
public record BasicExtendedWebSocketHandler(
    MessageListenerService messageListenerService,
    MessageSupplierService messageSupplierService
) implements ExtendedWebSocketHandler {

    private static final String WS_ROUTE = "/basic";

    @Override
    public String getPath() {
        return WS_ROUTE;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String token = (String) session.getAttributes().get("token");

        session.receive()
            .map(webSocketMessage -> new UserInputDTO(
                token,
                webSocketMessage.getPayloadAsText()
            ))
            .flatMap(messageListenerService::handleUserInput).subscribe();

        return session.send(
            messageSupplierService.getMessages(token)
                .doOnError(err -> System.out.println(err.getMessage()))
                .map(session::textMessage)
                .doFinally(sig -> messageListenerService.leaveAllChannels(token).subscribe()));

    }
}
