package io.gwynt.buffer;

import java.nio.ByteBuffer;

public interface RecvByteBufferAllocator {

    Handle newHandle();

    interface Handle {

        ByteBuffer allocate(ByteBufferPool byteBufferPool);

        int guess();

        void record(int bytesRead);
    }
}
