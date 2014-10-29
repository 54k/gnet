package io.gwynt.buffer;

import java.nio.ByteBuffer;

public interface ByteBufferPool {

    ByteBuffer acquire(int size, boolean direct);

    DynamicByteBuffer acquireDynamic(int size, boolean direct);

    void release(ByteBuffer buffer);

    void release(DynamicByteBuffer buffer);

    void clear();
}
