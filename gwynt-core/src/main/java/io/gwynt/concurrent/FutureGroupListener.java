package io.gwynt.concurrent;

@FunctionalInterface
public interface FutureGroupListener<V> extends FutureListener<FutureGroup<V>> {
}
