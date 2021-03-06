package com.github.iamhi.hiintegration.wildserver.core;

import com.github.iamhi.hiintegration.wildserver.config.TokenConfig;
import com.github.iamhi.hiintegration.wildserver.data.TokenRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public record TokenServiceImpl(
    TokenRepository tokenRepository,
    TokenConfig tokenConfig
) implements TokenService {
    @Override
    public Mono<String> createConnectionToken(String secret) {
        if (!tokenConfig.getSecret().equals(secret)) {
            return Mono.error(new Throwable("Error creating token"));
        }
        return tokenRepository.generateToken();
    }

    @Override
    public Mono<String> connectWithToken(String token) {
        return activateToken(token).flatMap(result
            -> Boolean.TRUE.equals(result)
            ? Mono.just(token)
            : Mono.error(new RuntimeException()));
    }

    private Mono<Boolean> activateToken(String token) {
        return tokenRepository.hasToken(token).flatMap(hasToken -> {
            if (Boolean.TRUE.equals(hasToken)) {
                return tokenRepository.activateToken(token);
            }

            return Mono.error(new RuntimeException());
        });
    }
}
