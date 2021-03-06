package io.gwynt;

import java.net.SocketAddress;

public class DefaultEnvelope<V, A extends SocketAddress> implements Envelope<V, A> {

    private V content;
    private A recipient;
    private A sender;

    public DefaultEnvelope(V content, A recipient, A sender) {
        this.content = content;
        this.recipient = recipient;
        this.sender = sender;
    }

    public DefaultEnvelope(V content, A recipient) {
        this(content, recipient, null);
    }

    @Override
    public V content() {
        return content;
    }

    @Override
    public A recipient() {
        return recipient;
    }

    @Override
    public A sender() {
        return sender;
    }

    @Override
    public String toString() {
        return getClass().getName() + "(recipient: " + recipient() + ", sender: " + sender() + ", content: " + content() + ')';
    }
}
