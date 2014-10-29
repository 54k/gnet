package io.gwynt;

import io.gwynt.concurrent.FutureListener;

public interface ChannelFutureListener extends FutureListener<ChannelFuture> {

    static ChannelFutureListener CLOSE_LISTENER = new ChannelFutureListener() {
        @Override
        public void onComplete(ChannelFuture future) {
            future.channel().close();
        }
    };
}
