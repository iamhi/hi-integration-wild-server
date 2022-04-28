package com.github.iamhi.hiintegration.wildserver.core;

import com.github.iamhi.hiintegration.wildserver.core.dto.UserInputDTO;
import com.github.iamhi.hiintegration.wildserver.data.ChannelRepository;
import com.github.iamhi.hiintegration.wildserver.data.RedisPubSubWrapper;
import com.github.iamhi.hiintegration.wildserver.data.TokenRepository;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
record MessageListenerServiceImpl(
    ChannelRepository channelRepository,
    RedisPubSubWrapper redisPubSubWrapper,
    TokenRepository tokenRepository,
    Gson gson
) implements MessageListenerService {

    private static final String DEFAULT_CHANNEL = "all";

    @Override
    public Mono<Boolean> handleUserInput(UserInputDTO userInput) {
        String channel = getChannelForUserInput(userInput);
        String message = getMessageForUserInput(userInput);

        return redisPubSubWrapper.publish(channel, message).map(result -> true);
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

    private String getChannelForUserInput(UserInputDTO userInput) {
        try {
            UserMessage userMessage = gson.fromJson(userInput.message(), UserMessage.class);

            return StringUtils.defaultString(userMessage.channel, DEFAULT_CHANNEL);
        } catch (JsonSyntaxException exception) {
            return DEFAULT_CHANNEL;
        }
    }

    private String getMessageForUserInput(UserInputDTO userInputDTO) {
        Map<String, String> messageProperties = gson.<Map<String, String>>fromJson(userInputDTO.message(), Map.class);

        messageProperties.put("token", userInputDTO.token());


        return gson().toJson(messageProperties);
    }

    @Data
    private static class UserMessage {

        private String channel;

        private String data;
    }
}
