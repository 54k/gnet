package io.gwynt;

import io.gwynt.concurrent.Promise;

public interface ChannelPromise extends ChannelFuture, Promise<Void> {

    boolean trySuccess();

    ChannelPromise setSuccess();

    @Override
    ChannelPromise setSuccess(Void result);

    @Override
    ChannelPromise setFailure(Throwable error);

    @Override
    ChannelPromise chainPromises(Promise<Void>... promises);

    @Override
    ChannelPromise chainPromise(Promise<Void> promise);
}
