package com.github.iamhi.hiintegration.wildserver.data;

import java.util.List;

public interface ChannelRepository {

    boolean createChannel(String channelName);

    boolean channelExists(String channelName);

    boolean addToken(String channelName, String token);

    boolean removeToken(String channelName, String token);

    boolean channelHasToken(String channelName, String token);

    List<String> getTokenChannels(String token);
}
