package io.gwynt.concurrent;

import java.util.concurrent.TimeUnit;

public interface ProcessFuture extends ScheduledFuture<ProcessStatus> {

    @Override
    ProcessFuture addListener(FutureListener<? extends Future<? super ProcessStatus>> futureListener);

    @Override
    ProcessFuture addListeners(FutureListener<? extends Future<? super ProcessStatus>>... futureListeners);

    @Override
    ProcessFuture removeListener(FutureListener<? extends Future<? super ProcessStatus>> futureListener);

    @Override
    ProcessFuture removeListeners(FutureListener<? extends Future<? super ProcessStatus>>... futureListeners);

    @Override
    ProcessFuture sync() throws InterruptedException;

    @Override
    ProcessFuture sync(long timeout, TimeUnit unit) throws InterruptedException;

    @Override
    ProcessFuture sync(long timeoutMillis) throws InterruptedException;
}
