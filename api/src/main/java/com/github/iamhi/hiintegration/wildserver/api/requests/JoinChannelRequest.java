package com.github.iamhi.hiintegration.wildserver.api.requests;

public record JoinChannelRequest(
    String channel,
    String token
) {
}
