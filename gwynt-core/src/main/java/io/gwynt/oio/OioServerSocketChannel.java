package io.gwynt.oio;

import io.gwynt.ChannelConfig;
import io.gwynt.ChannelException;
import io.gwynt.ChannelOption;
import io.gwynt.ChannelOutboundBuffer;
import io.gwynt.ChannelPromise;
import io.gwynt.ServerChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.List;

public class OioServerSocketChannel extends AbstractOioChannel implements ServerChannel {

    @SuppressWarnings("unused")
    public OioServerSocketChannel() {
        super(newSocket());
    }

    private static ServerSocket newSocket() {
        try {
            return new ServerSocket();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override
    protected AbstractOioUnsafe newUnsafe() {
        return new OioServerSocketChannelUnsafe();
    }

    @Override
    protected ChannelConfig newConfig() {
        return new OioServerSocketChannelConfig(this);
    }

    @Override
    public OioServerSocketChannelConfig config() {
        return (OioServerSocketChannelConfig) super.config();
    }

    @Override
    public ServerSocket javaChannel() {
        return (ServerSocket) super.javaChannel();
    }

    protected class OioServerSocketChannelUnsafe extends AbstractOioUnsafe {

        @Override
        public boolean isActive() {
            return isOpen() && javaChannel().isBound();
        }

        @Override
        public boolean isOpen() {
            return !javaChannel().isClosed();
        }

        @Override
        protected void doBind(InetSocketAddress address, ChannelPromise channelPromise) throws Exception {
            javaChannel().bind(address, config().getOption(ChannelOption.SO_BACKLOG));
            javaChannel().setSoTimeout(SO_TIMEOUT);
        }

        @Override
        protected void writeRequested() {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void doConnect(InetSocketAddress address, ChannelPromise channelPromise) throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        protected int doReadMessages(List<Object> messages) throws Exception {
            if (!isActive()) {
                return -1;
            }

            try {
                Socket ch = javaChannel().accept();
                try {
                    ch.setSoTimeout(SO_TIMEOUT);
                    OioSocketChannel channel = new OioSocketChannel(OioServerSocketChannel.this, ch);
                    messages.add(channel);
                    return 1;
                } catch (IOException e) {
                    exceptionCaught(e);
                    ch.close();
                }
            } catch (SocketTimeoutException ignore) {
            }
            return 0;
        }

        @Override
        protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public void closeForcibly() {
            try {
                javaChannel().close();
            } catch (IOException ignore) {
            }
        }

        @Override
        public SocketAddress getLocalAddress() throws Exception {
            return javaChannel().getLocalSocketAddress();
        }

        @Override
        public SocketAddress getRemoteAddress() throws Exception {
            return null;
        }
    }
}
