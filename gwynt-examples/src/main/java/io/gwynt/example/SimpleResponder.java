package io.gwynt.example;

import io.gwynt.AbstractHandler;
import io.gwynt.ChannelFuture;
import io.gwynt.ChannelFutureListener;
import io.gwynt.pipeline.HandlerContext;

import java.util.Date;

public class SimpleResponder extends AbstractHandler<String, Object> {

    @Override
    public void onMessageReceived(HandlerContext context, String message) {
        context.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain; charset=utf-8\r\n\r\n");
        context.write(new Date().toString() + "\r\n").addListener(new ChannelFutureListener() {
            @Override
            public void onComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    future.channel().close();
                }
            }
        });
    }
}
