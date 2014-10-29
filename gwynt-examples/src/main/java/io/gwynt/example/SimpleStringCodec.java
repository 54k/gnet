package io.gwynt.example;

import io.gwynt.AbstractHandler;
import io.gwynt.ChannelPromise;
import io.gwynt.pipeline.HandlerContext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class SimpleStringCodec extends AbstractHandler<byte[], String> {

    private final Charset charset = Charset.forName("UTF-8");

    @Override
    public void onMessageReceived(HandlerContext context, byte[] message) {
        ByteBuffer buffer = ByteBuffer.wrap(message);
        CharBuffer charBuffer = charset.decode(buffer);
        buffer.clear();
        context.fireMessageReceived(charBuffer.toString());
    }

    @Override
    public void onMessageSent(HandlerContext context, String message, ChannelPromise channelPromise) {
        ByteBuffer buffer = charset.encode(message);
        byte[] messageBytes = new byte[buffer.limit()];
        buffer.get(messageBytes);
        buffer.clear();
        context.write(messageBytes, channelPromise);
    }
}
