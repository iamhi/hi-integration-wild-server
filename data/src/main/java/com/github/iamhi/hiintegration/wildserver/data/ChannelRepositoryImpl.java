package com.github.iamhi.hiintegration.wildserver.data;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Repository
public class ChannelRepositoryImpl implements ChannelRepository {

    Map<String, HashSet<String>> channels = new HashMap<>();

    @Override
    public boolean createChannel(String channelName) {
        if (channels.containsKey(channelName)) {
            return false;
        }

        channels.put(channelName, new HashSet<>());

        return true;
    }

    @Override
    public boolean channelExists(String channelName) {
        return channels.containsKey(channelName);
    }

    @Override
    public boolean addToken(String channelName, String token) {
        channels.computeIfAbsent(channelName, key -> new HashSet<>());

        channels.get(channelName).add(token);

        return true;
    }

    @Override
    public boolean removeToken(String channelName, String token) {
        channels.computeIfPresent(channelName,
            (key, set) -> {
                set.remove(token);

                return set;
            });

        return false;
    }

    @Override
    public boolean channelHasToken(String channelName, String token) {
        return channels.containsKey(channelName) && channels.get(channelName).contains(token);
    }

    @Override
    public List<String> getTokenChannels(String token) {
        return channels.keySet().stream().filter(channelName ->
            channels.get(channelName).contains(token)).toList();
    }
}
