package io.gwynt.oio;

import io.gwynt.concurrent.DefaultThreadFactory;
import io.gwynt.ThreadPerChannelEventLoopGroup;

public final class OioEventLoopGroup extends ThreadPerChannelEventLoopGroup {

    public OioEventLoopGroup() {
        this(0);
    }

    public OioEventLoopGroup(int maxChannels) {
        super(maxChannels, new DefaultThreadFactory("gwynt-oio-eventloop"));
    }
}
