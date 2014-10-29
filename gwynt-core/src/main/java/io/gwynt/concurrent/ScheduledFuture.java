package io.gwynt.concurrent;

import java.util.concurrent.TimeUnit;

public interface ScheduledFuture<V> extends io.gwynt.concurrent.Future<V>, java.util.concurrent.ScheduledFuture<V> {

    public long getDelayNanos();

    public long getDelayNanos(long timeNanos);

    public long getDelay(long time, TimeUnit unit);
}
