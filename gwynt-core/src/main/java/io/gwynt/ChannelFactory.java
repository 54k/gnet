package io.gwynt;

public interface ChannelFactory<I extends Channel> {

    I createChannel(Class<? extends I> channelClass);
}
