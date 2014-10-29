package io.gwynt.concurrent;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

class ProcessTask extends ScheduledTask<ProcessStatus> implements ProcessFuture {

    ProcessTask(EventExecutor eventExecutor, Callable<ProcessStatus> task, long delay, Queue<ScheduledTask<?>> delayedTaskQueue) {
        this(eventExecutor, task, delay, 0, delayedTaskQueue);
    }

    ProcessTask(EventExecutor eventExecutor, Callable<ProcessStatus> task, long delay, long period, Queue<ScheduledTask<?>> delayedTaskQueue) {
        super(eventExecutor, task, delay, period, delayedTaskQueue);
    }

    @Override
    public void run() {
        assert executor().inExecutorThread();
        try {
            if (period == 0) {
                if (!isCancelled()) {
                    processResult();
                }
            } else {
                if (!isCancelled()) {
                    processResult();
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
        } catch (Throwable cause) {
            setFailureInternal(cause);
        }
    }

    private void processResult() throws Exception {
        ProcessStatus result = task.call();
        switch (result) {
            case COMPLETED:
                setSuccessInternal(result);
                break;
            case RUNNING:
                if (!isCancelled()) {
                    delayedTaskQueue.add(this);
                }
                break;
            default:
                throw new Error();
        }
    }

    @Override
    public ProcessFuture addListener(FutureListener<? extends Future<? super ProcessStatus>> futureListener) {
        super.addListener(futureListener);
        return this;
    }

    @Override
    public ProcessFuture addListeners(FutureListener<? extends Future<? super ProcessStatus>>... futureListeners) {
        super.addListeners(futureListeners);
        return this;
    }

    @Override
    public ProcessFuture removeListener(FutureListener<? extends Future<? super ProcessStatus>> futureListener) {
        super.removeListener(futureListener);
        return this;
    }

    @Override
    public ProcessFuture removeListeners(FutureListener<? extends Future<? super ProcessStatus>>... futureListeners) {
        super.removeListeners(futureListeners);
        return this;
    }

    @Override
    public ProcessFuture sync(long timeout, TimeUnit unit) throws InterruptedException {
        super.sync(timeout, unit);
        return this;
    }

    @Override
    public ProcessFuture sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override
    public ProcessFuture sync(long timeoutMillis) throws InterruptedException {
        super.sync(timeoutMillis);
        return this;
    }
}
