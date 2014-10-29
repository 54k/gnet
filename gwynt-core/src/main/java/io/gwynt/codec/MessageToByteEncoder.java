package io.gwynt.codec;

import io.gwynt.AbstractHandler;
import io.gwynt.ChannelPromise;
import io.gwynt.buffer.ByteBufferPool;
import io.gwynt.buffer.DynamicByteBuffer;
import io.gwynt.pipeline.HandlerContext;

public abstract class MessageToByteEncoder<O> extends AbstractHandler<Object, O> {

    @Override
    public void onMessageSent(HandlerContext context, O message, ChannelPromise channelPromise) {
        ByteBufferPool allocator = context.channel().config().getByteBufferPool();
        DynamicByteBuffer out = allocator.acquireDynamic(0, preferDirectBuffer());

        try {
            encode(context, message, out);
            out.flip();
            byte[] bytes = new byte[out.remaining()];
            out.get(bytes);
            context.write(bytes, channelPromise);
        } catch (EncoderException e) {
            throw e;
        } catch (Throwable e) {
            throw new EncoderException(e);
        } finally {
            allocator.release(out);
        }
    }

    protected boolean preferDirectBuffer() {
        return false;
    }

    protected abstract void encode(HandlerContext context, O message, DynamicByteBuffer out);
}
