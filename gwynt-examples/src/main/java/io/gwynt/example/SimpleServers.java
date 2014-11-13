package io.gwynt.example;

import io.gwynt.Bootstrap;
import io.gwynt.Channel;
import io.gwynt.EventLoopGroup;
import io.gwynt.concurrent.DefaultFutureGroup;
import io.gwynt.concurrent.Future;
import io.gwynt.concurrent.FutureGroup;
import io.gwynt.nio.NioEventLoopGroup;
import io.gwynt.nio.NioServerSocketChannel;
import io.gwynt.oio.OioEventLoopGroup;
import io.gwynt.oio.OioServerSocketChannel;

import java.util.Arrays;

public class SimpleServers implements Runnable {

    @Override
    public void run() {
        EventLoopGroup oioEventLoop = new OioEventLoopGroup();
        EventLoopGroup nioEventLoop = new NioEventLoopGroup();

        SimpleResponder httpResponder;
        httpResponder = new SimpleResponder();
        SimpleStringCodec simpleStringCodec = new SimpleStringCodec();

        Bootstrap oioBootstrap = new Bootstrap().channelClass(OioServerSocketChannel.class).group(oioEventLoop)
                .addChildHandler(simpleStringCodec).addChildHandler(httpResponder);
        Bootstrap nioBootstrap = new Bootstrap().channelClass(NioServerSocketChannel.class).group(nioEventLoop)
                .addChildHandler(simpleStringCodec).addChildHandler(httpResponder);

        try {
            Future oioCloseFuture = oioBootstrap.bind(3000).sync().channel().closeFuture();
            Future nioCloseFuture = nioBootstrap.bind(3001).sync().channel().closeFuture();
            FutureGroup<?> futures = new DefaultFutureGroup<Channel>(Arrays.asList(oioCloseFuture, nioCloseFuture));
            futures.sync();
        } catch (InterruptedException ignore) {
        }
    }

}
