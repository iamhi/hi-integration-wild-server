package com.github.iamhi.hiintegration.wildserver.core;

import com.github.iamhi.hiintegration.wildserver.core.dto.UserInputDTO;
import com.github.iamhi.hiintegration.wildserver.data.ChannelRepository;
import com.github.iamhi.hiintegration.wildserver.data.RedisPubSubWrapper;
import com.github.iamhi.hiintegration.wildserver.data.TokenRepository;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
record MessageListenerServiceImpl(
    ChannelRepository channelRepository,
    RedisPubSubWrapper redisPubSubWrapper,
    TokenRepository tokenRepository,
    Gson gson
) implements MessageListenerService {
    @Override
    public Mono<Boolean> handleUserInput(UserInputDTO userInput) {
        System.out.println(userInput.message());

        return redisPubSubWrapper.publish("all", userInput.message()).map(result -> true);
    }

    @Override
    public Mono<Boolean> joinChannel(String channelName, String token) {
        return tokenRepository.hasToken(token).flatMap(
            result -> {
                if (Boolean.TRUE.equals(result)) {
                    if (!channelRepository.channelExists(channelName)) {
                        redisPubSubWrapper.subscribe(channelName).subscribe();
                    }

                    return Mono.just(channelRepository.addToken(channelName, token));
                }

                return Mono.error(new RuntimeException());
            }
        ).switchIfEmpty(Mono.error(new RuntimeException()));
    }

    @Override
    public Mono<Boolean> leaveChannel(String channelName, String token) {
        return Mono.just(channelRepository.removeToken(channelName, token));
    }

    @Override
    public Mono<Void> leaveAllChannels(String token) {
        channelRepository
            .getTokenChannels(token)
            .forEach(channel -> channelRepository.removeToken(channel, token));

        return Mono.empty();
    }
}
