package com.github.iamhi.hiintegration.wildserver.core;

import com.github.iamhi.hiintegration.wildserver.core.dto.UserInputDTO;
import reactor.core.publisher.Mono;

public interface MessageListenerService {

    Mono<Boolean> handleUserInput(UserInputDTO userInput);

    Mono<Boolean> joinChannel(String channelName, String token);

    Mono<Boolean> leaveChannel(String channelName, String token);

    Mono<Void> leaveAllChannels(String token);
}
