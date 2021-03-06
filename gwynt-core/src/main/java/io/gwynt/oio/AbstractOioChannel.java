package io.gwynt.oio;

import io.gwynt.AbstractChannel;
import io.gwynt.Channel;
import io.gwynt.ChannelPromise;
import io.gwynt.EventLoop;
import io.gwynt.ServerChannel;
import io.gwynt.ThreadPerChannelEventLoop;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOioChannel extends AbstractChannel {

    protected static final int SO_TIMEOUT = 1000;

    protected AbstractOioChannel(Object ch) {
        this(null, ch);
    }

    protected AbstractOioChannel(Channel parent, Object ch) {
        super(parent, ch);
    }

    @Override
    protected boolean isEventLoopCompatible(EventLoop eventLoop) {
        return eventLoop instanceof ThreadPerChannelEventLoop;
    }

    @Override
    protected abstract AbstractOioUnsafe newUnsafe();

    protected abstract class AbstractOioUnsafe extends AbstractUnsafe {

        private final Runnable readTask = () -> {
            setReadPending(false);
            if (!config().isAutoRead()) {
                return;
            }
            read();
        };

        private final Runnable writeTask = this::flush;

        private final List<Object> messages = new ArrayList<>(1);
        private volatile boolean readPending;

        private boolean isReadPending() {
            return readPending;
        }

        private void setReadPending(boolean readPending) {
            this.readPending = readPending;
        }

        @Override
        protected void readRequested() {
            if (isReadPending()) {
                return;
            }

            setReadPending(true);
            invokeLater(readTask);
        }

        @Override
        protected void writeRequested() {
            if (eventLoop().inExecutorThread()) {
                writeTask.run();
            } else {
                invokeLater(writeTask);
            }
        }

        @Override
        protected void afterRegister() {
            // NO OP
        }

        @Override
        protected void afterUnregister() {
            // NO OP
        }

        @Override
        public void connect(InetSocketAddress address, ChannelPromise channelPromise) {
            try {
                boolean wasActive = isActive();
                doConnect(address, channelPromise);
                safeSetSuccess(channelPromise);
                if (!wasActive && isActive()) {
                    pipeline().fireOpen();
                    if (config().isAutoRead()) {
                        readRequested();
                    }
                }
            } catch (Throwable t) {
                safeSetFailure(channelPromise, t);
                closeForcibly();
            }
        }

        protected abstract void doConnect(InetSocketAddress address, ChannelPromise channelPromise) throws Exception;

        protected void read() {
            Throwable error = null;
            boolean closed = false;
            try {
                int messagesRead = 0;
                try {
                    messagesRead = doReadMessages(messages);
                    if (messagesRead < 0) {
                        closed = true;
                    }
                } catch (Throwable e) {
                    error = e;
                }

                for (int i = 0; i < messagesRead; i++) {
                    pipeline().fireMessageReceived(messages.get(i));
                }

                if (error != null) {
                    if (error instanceof IOException) {
                        closed = !(AbstractOioChannel.this instanceof ServerChannel);
                    }
                    pipeline().fireExceptionCaught(error);
                }

                if (closed && isOpen()) {
                    close(voidPromise());
                }
                messages.clear();
            } finally {
                if (isActive() && config().isAutoRead()) {
                    readRequested();
                }
            }
        }

        protected abstract int doReadMessages(List<Object> messages) throws Exception;
    }
}
