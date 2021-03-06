package io.gwynt.group;

import io.gwynt.concurrent.EventExecutor;
import io.gwynt.Channel;

import java.util.Set;

public interface ChannelGroup extends Set<Channel>, Comparable<ChannelGroup> {

    String name();

    <T> T accept(ChannelGroupVisitor<T> visitor);

    ChannelGroupFuture read(ChannelMatcher channelMatcher);

    ChannelGroupFuture read();

    ChannelGroupFuture write(Object message, ChannelMatcher channelMatcher);

    ChannelGroupFuture write(Object message);

    ChannelGroupFuture close(ChannelMatcher channelMatcher);

    ChannelGroupFuture close();

    ChannelGroupFuture unregister(ChannelMatcher channelMatcher);

    ChannelGroupFuture unregister();

    ChannelGroup newGroup(ChannelMatcher channelMatcher);

    ChannelGroup newGroup(ChannelMatcher channelMatcher, String name);

    ChannelGroup newGroup(ChannelMatcher channelMatcher, EventExecutor eventExecutor);

    ChannelGroup newGroup(ChannelMatcher channelMatcher, String name, EventExecutor eventExecutor);
}
