package com.github.iamhi.hiintegration.wildserver.data;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public record TokenRepositoryImpl(
    RedisRepository redisRepository
) implements TokenRepository {

    private static final String ACTIVE_TOKEN_STATE = "active";

    @Override
    public Mono<String> generateToken() {
        String newToken = UUID.randomUUID().toString();

        return redisRepository
            .getReactiveConnection()
            .set(newToken, Boolean.TRUE.toString())
            .map(result -> newToken);
    }

    @Override
    public Mono<Boolean> hasToken(String token) {
        return redisRepository
            .getReactiveConnection()
            .get(token)
            .map(StringUtils::isNotBlank);
    }

    @Override
    public Mono<Boolean> removeToken(String token) {
        return redisRepository
            .getReactiveConnection()
            .getdel(token)
            .map(StringUtils::isEmpty);
    }

    @Override
    public Mono<Boolean> activateToken(String token) {
        return redisRepository
            .getReactiveConnection()
            .get(token)
            .flatMap(tokenState -> {
                if (!ACTIVE_TOKEN_STATE.equals(tokenState)) {
                    return redisRepository
                        .getReactiveConnection()
                        .set(token, ACTIVE_TOKEN_STATE).map(result -> true);
                }

                return Mono.error(new RuntimeException());
            });
    }
}
