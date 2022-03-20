package com.github.iamhi.hiintegration.wildserver.data;

import io.lettuce.core.pubsub.api.reactive.ChannelMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RedisPubSubWrapper {

    Mono<Void> subscribe(String ...channels);

    Mono<Void> unsubscribe(String ...channels);

    Mono<Long> publish(String channel, String message);

    Flux<ChannelMessage<String, String>> observe();
}
