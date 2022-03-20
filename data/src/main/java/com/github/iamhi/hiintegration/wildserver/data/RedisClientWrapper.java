package com.github.iamhi.hiintegration.wildserver.data;

import com.github.iamhi.hiintegration.wildserver.config.RedisConfig;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.pubsub.api.reactive.ChannelMessage;
import io.lettuce.core.pubsub.api.reactive.RedisPubSubReactiveCommands;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RedisClientWrapper implements RedisRepository, RedisPubSubWrapper {

    RedisReactiveCommands<String, String> reactiveCommands;

    RedisPubSubReactiveCommands<String, String> reactiveSubCommands;

    RedisPubSubReactiveCommands<String, String> reactivePubCommands;

    public RedisClientWrapper(RedisConfig redisConfig) {
        RedisClient redisClient = RedisClient.create("redis://" + redisConfig.getHost() + ":" + redisConfig.getPort());
        StatefulRedisConnection<String, String> connection = redisClient.connect();

        reactiveSubCommands = redisClient.connectPubSub().reactive();
        reactivePubCommands = redisClient.connectPubSub().reactive();

        reactiveCommands = connection.reactive();
    }

    @Override
    public Mono<Void> subscribe(String... channels) {
        return reactiveSubCommands.subscribe(channels);
    }

    @Override
    public Mono<Void> unsubscribe(String... channels) {
        return reactiveSubCommands.unsubscribe(channels);
    }

    @Override
    public Mono<Long> publish(String channel, String message) {
        return reactivePubCommands.publish(channel, message);
    }

    @Override
    public Flux<ChannelMessage<String, String>> observe() {
        return reactiveSubCommands.observeChannels();
    }

    @Override
    public RedisReactiveCommands<String, String> getReactiveConnection() {
        return reactiveCommands;
    }
}
