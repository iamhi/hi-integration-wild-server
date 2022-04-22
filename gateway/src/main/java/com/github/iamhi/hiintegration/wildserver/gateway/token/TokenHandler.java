package com.github.iamhi.hiintegration.wildserver.gateway.token;

import com.github.iamhi.hiintegration.wildserver.api.requests.TokenRequest;
import com.github.iamhi.hiintegration.wildserver.config.TokenConfig;
import com.github.iamhi.hiintegration.wildserver.core.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public record TokenHandler(
    TokenService tokenService,
    TokenConfig tokenConfig
) {
    Mono<ServerResponse> createToken(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TokenRequest.class)
            .map(TokenRequest::secret)
            .flatMap(tokenService::createConnectionToken)
            .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }
}
