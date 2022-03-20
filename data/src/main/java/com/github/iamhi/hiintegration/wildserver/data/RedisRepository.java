package com.github.iamhi.hiintegration.wildserver.data;

import io.lettuce.core.api.reactive.RedisReactiveCommands;

public interface RedisRepository {

    RedisReactiveCommands<String, String> getReactiveConnection();
}
