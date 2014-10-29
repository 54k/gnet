package io.gwynt;

import java.net.SocketAddress;

public interface Envelope<V, A extends SocketAddress> {

    V content();

    A recipient();

    A sender();
}
