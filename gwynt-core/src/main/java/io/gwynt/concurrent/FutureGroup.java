package io.gwynt.concurrent;

import java.util.concurrent.TimeUnit;

public interface FutureGroup<V> extends Future<V>, Iterable<io.gwynt.concurrent.Future<V>> {

    @Override
    FutureGroup<V> addListener(FutureListener<? extends io.gwynt.concurrent.Future<? super V>> futureListener);

    @Override
    FutureGroup<V> addListeners(FutureListener<? extends io.gwynt.concurrent.Future<? super V>>... futureListeners);

    @Override
    FutureGroup<V> removeListener(FutureListener<? extends io.gwynt.concurrent.Future<? super V>> futureListener);

    @Override
    FutureGroup<V> removeListeners(FutureListener<? extends io.gwynt.concurrent.Future<? super V>>... futureListeners);

    @Override
    FutureGroup<V> await() throws InterruptedException;

    @Override
    FutureGroup<V> sync() throws InterruptedException;

    @Override
    FutureGroup<V> sync(long timeout, TimeUnit unit) throws InterruptedException;

    @Override
    FutureGroup<V> sync(long timeoutMillis) throws InterruptedException;
}
