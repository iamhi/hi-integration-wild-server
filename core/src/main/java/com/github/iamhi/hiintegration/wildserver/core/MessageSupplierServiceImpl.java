package com.github.iamhi.hiintegration.wildserver.core;

import com.github.iamhi.hiintegration.wildserver.data.ChannelRepository;
import com.github.iamhi.hiintegration.wildserver.data.RedisPubSubWrapper;
import io.lettuce.core.pubsub.api.reactive.ChannelMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
record MessageSupplierServiceImpl(
    RedisPubSubWrapper redisPubSubWrapper,
    ChannelRepository channelRepository
) implements MessageSupplierService {
    @Override
    public Flux<String> getMessages(String token) {
        return redisPubSubWrapper.observe()
            .filter(channelMessage ->
                channelRepository
                    .getTokenChannels(token)
                    .contains(channelMessage.getChannel()))
            .map(ChannelMessage::getMessage);
    }
}
