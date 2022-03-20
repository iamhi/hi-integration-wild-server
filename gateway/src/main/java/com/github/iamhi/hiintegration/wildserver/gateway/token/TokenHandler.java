package com.github.iamhi.hiintegration.wildserver.gateway.token;

import com.github.iamhi.hiintegration.wildserver.core.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public record TokenHandler(
    TokenService tokenService
) {

    Mono<ServerResponse> createToken(ServerRequest serverRequest) {
        return ServerResponse.ok().body(tokenService.createConnectionToken(), String.class);
    }
}
