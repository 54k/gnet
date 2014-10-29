package io.gwynt.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public final class DefaultFutureGroup<V> extends io.gwynt.concurrent.AbstractFutureGroup<V, io.gwynt.concurrent.FutureGroup<V>> implements io.gwynt.concurrent.FutureGroup<V> {

    @SuppressWarnings("FieldCanBeLocal")
    private final FutureListener<io.gwynt.concurrent.Future<V>> futureListener = new FutureListener<Future<V>>() {
        @Override
        public void onComplete(Future future) {
            count(future);
        }
    };
    private Set<io.gwynt.concurrent.Future<V>> futures;

    public DefaultFutureGroup(Collection<? extends io.gwynt.concurrent.Future<V>> futures) {
        this(null, futures);
    }

    public DefaultFutureGroup(EventExecutor eventExecutor, Collection<? extends io.gwynt.concurrent.Future<V>> futures) {
        super(eventExecutor);
        if (futures == null) {
            throw new IllegalArgumentException("futures");
        }

        Set<io.gwynt.concurrent.Future<V>> futureSet = new LinkedHashSet<>(futures.size());
        for (io.gwynt.concurrent.Future<V> f : futures) {
            futureSet.add(f);
        }
        this.futures = Collections.unmodifiableSet(futureSet);

        for (io.gwynt.concurrent.Future<V> f : futures) {
            f.addListener(futureListener);
        }

        if (this.futures.isEmpty()) {
            setSuccess0();
        }
    }

    @Override
    protected void notify0() {
        if (successCount() + failureCount() < futures.size()) {
            return;
        }
        assert successCount() + failureCount() == futures.size();
        if (failureCount() > 0) {
            List<Entry<io.gwynt.concurrent.Future, Throwable>> failed = new ArrayList<>(failureCount());
            for (io.gwynt.concurrent.Future f : futures) {
                if (!f.isSuccess()) {
                    failed.add(new DefaultEntry<>(f, f.getCause()));
                }
            }
            setFailure0(new io.gwynt.concurrent.FutureGroupException(failed));
        } else {
            setSuccess0();
        }
    }

    @Override
    public Iterator<io.gwynt.concurrent.Future<V>> iterator() {
        return futures.iterator();
    }
}
