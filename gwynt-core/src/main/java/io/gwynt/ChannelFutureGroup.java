package io.gwynt;

import io.gwynt.concurrent.Future;
import io.gwynt.concurrent.FutureListener;

import java.util.concurrent.TimeUnit;

public interface ChannelFutureGroup extends Future<Void>, Iterable<ChannelFuture> {

    @Override
    ChannelFutureGroup addListener(FutureListener<? extends Future<? super Void>> futureListener);

    @Override
    ChannelFutureGroup addListeners(FutureListener<? extends Future<? super Void>>... futureListeners);

    @Override
    ChannelFutureGroup removeListener(FutureListener<? extends Future<? super Void>> futureListener);

    @Override
    ChannelFutureGroup removeListeners(FutureListener<? extends Future<? super Void>>... futureListeners);

    @Override
    ChannelFutureGroup await() throws InterruptedException;

    @Override
    ChannelFutureGroup sync() throws InterruptedException;

    @Override
    ChannelFutureGroup sync(long timeout, TimeUnit unit) throws InterruptedException;

    @Override
    ChannelFutureGroup sync(long timeoutMillis) throws InterruptedException;
}
