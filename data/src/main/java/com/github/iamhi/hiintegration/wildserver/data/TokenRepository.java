package com.github.iamhi.hiintegration.wildserver.data;

import reactor.core.publisher.Mono;

public interface TokenRepository {

    Mono<String> generateToken();

    Mono<Boolean> hasToken(String token);

    Mono<Boolean> removeToken(String token);

    Mono<Boolean> activateToken(String token);
}
