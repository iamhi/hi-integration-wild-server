package com.github.iamhi.hiintegration.wildserver.core;

import reactor.core.publisher.Mono;

public interface TokenService {

    Mono<String> createConnectionToken();

    Mono<String> connectWithToken(String token);
}
