package io.gwynt;

import io.gwynt.concurrent.EventExecutorGroup;

public interface EventLoopGroup extends EventExecutorGroup, EventLoop {

    @Override
    EventLoop next();
}
