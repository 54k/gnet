package io.gwynt.buffer;

import java.nio.ByteBuffer;

public interface ByteBufferAllocator {

    ByteBuffer allocate(int capacity);

    void release(ByteBuffer buffer);
}
