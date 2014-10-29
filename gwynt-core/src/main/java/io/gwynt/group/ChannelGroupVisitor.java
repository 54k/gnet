package io.gwynt.group;

public interface ChannelGroupVisitor<T> {

    T visit(ChannelGroup channelGroup);
}
