package io.gwynt.concurrent;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

class ScheduledTask<V> extends PromiseTask<V> implements ScheduledFuture<V> {

    private static final long ORIGIN = System.nanoTime();
    private static final AtomicLong SEQUENCER = new AtomicLong(0);

    long seq = SEQUENCER.getAndIncrement();

    /**
     * Period in nanoseconds for repeating tasks.  A positive
     * value indicates fixed-rate execution.  A negative value
     * indicates fixed-delay execution.  A value of 0 indicates a
     * non-repeating task.
     */
    long period;
    long triggerTime;
    Queue<ScheduledTask<?>> delayedTaskQueue;

    ScheduledTask(EventExecutor eventExecutor, Callable<V> task, long delay, Queue<ScheduledTask<?>> delayedTaskQueue) {
        this(eventExecutor, task, delay, 0, delayedTaskQueue);
    }

    ScheduledTask(EventExecutor eventExecutor, Callable<V> task, long delay, long period, Queue<ScheduledTask<?>> delayedTaskQueue) {
        super(eventExecutor, task);
        this.triggerTime = delay;
        this.period = period;
        this.delayedTaskQueue = delayedTaskQueue;
    }

    public static long nanos() {
        return System.nanoTime() - ORIGIN;
    }

    public static long triggerTime(long delayNanos) {
        return nanos() + delayNanos;
    }

    public long triggerTime() {
        return triggerTime;
    }

    @Override
    public long getDelayNanos() {
        return Math.max(0, triggerTime - nanos());
    }

    @Override
    public long getDelayNanos(long timeNanos) {
        return Math.max(0, triggerTime - (timeNanos - ORIGIN));
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(getDelayNanos(), TimeUnit.NANOSECONDS);
    }

    @Override
    public long getDelay(long time, TimeUnit unit) {
        return unit.convert(getDelayNanos(unit.toNanos(time)), TimeUnit.NANOSECONDS);
    }

    @Override
    public void run() {
        assert executor().inExecutorThread();
        try {
            if (period == 0) {
                if (setUncancellableInternal()) {
                    V result = task.call();
                    setSuccessInternal(result);
                }
            } else {
                if (!isCancelled()) {
                    task.call();
                    if (!executor().isShutdown()) {
                        long p = period;

                        if (p > 0) {
                            triggerTime += p;
                        } else {
                            triggerTime = triggerTime(-p);
                        }

                        if (!isCancelled()) {
                            delayedTaskQueue.add(this);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            setFailureInternal(t);
        }
    }

    @Override
    public int compareTo(Delayed o) {
        if (o == null) {
            throw new IllegalArgumentException("o");
        }

        if (this == o) {
            return 0;
        }

        if (o instanceof ScheduledTask) {
            ScheduledTask<?> that = (ScheduledTask<?>) o;
            long diff = getDelayNanos() - that.getDelayNanos();
            if (diff < 0) {
                return -1;
            } else if (diff > 0) {
                return 1;
            } else if (seq < that.seq) {
                return -1;
            } else {
                return 1;
            }
        }

        long d = (getDelayNanos() - o.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }
}
