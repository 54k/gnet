package io.gwynt;

import io.gwynt.concurrent.EventExecutor;
import io.gwynt.pipeline.HandlerContextInvoker;

public interface EventLoop extends EventExecutor {

    HandlerContextInvoker asInvoker();

    @Override
    EventLoopGroup parent();

    ChannelFuture register(Channel channel);

    ChannelFuture register(ChannelPromise channelPromise);

    ChannelFuture unregister(Channel channel);

    ChannelFuture unregister(ChannelPromise channelPromise);
}
