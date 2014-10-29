package io.gwynt;

import io.gwynt.pipeline.HandlerContext;

public abstract class DatagramHandler extends AbstractHandler<Datagram, Datagram> {

    @Override
    public void onMessageReceived(HandlerContext context, Datagram message) {
        context.fireMessageReceived(message);
    }

    @Override
    public void onMessageSent(HandlerContext context, Datagram message, ChannelPromise channelPromise) {
        context.write(message, channelPromise);
    }
}
