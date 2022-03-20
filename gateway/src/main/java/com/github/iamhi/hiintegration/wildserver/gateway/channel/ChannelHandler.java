package com.github.iamhi.hiintegration.wildserver.gateway.channel;

import com.github.iamhi.hiintegration.wildserver.api.requests.JoinChannelRequest;
import com.github.iamhi.hiintegration.wildserver.core.MessageListenerService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public record ChannelHandler(MessageListenerService messageListenerService) {

    Mono<ServerResponse> joinChannel(ServerRequest serverRequest) {
        return ServerResponse.ok().body(
            serverRequest.bodyToMono(JoinChannelRequest.class)
                .flatMap(joinChannelRequest ->
                    messageListenerService.joinChannel(joinChannelRequest.channel(), joinChannelRequest.token()))
                .flatMap(result -> Boolean.TRUE.equals(result) ? Mono.just("OK") : Mono.just("NOK")),
            String.class
        );
    }
}
