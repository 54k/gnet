package io.gwynt;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;

public interface MulticastChannel extends Channel {

    @Override
    InetSocketAddress getLocalAddress();

    @Override
    InetSocketAddress getRemoteAddress();

    ChannelFuture joinGroup(InetAddress multicastAddress);

    ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface);

    ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise channelPromise);

    ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise channelPromise);

    ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source);

    ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise channelPromise);

    ChannelFuture leaveGroup(InetAddress multicastAddress);

    ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface);

    ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise channelPromise);

    ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise channelPromise);

    ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source);

    ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise channelPromise);

    ChannelFuture block(InetAddress multicastAddress, InetAddress source);

    ChannelFuture block(InetAddress multicastAddress, InetAddress source, ChannelPromise channelPromise);

    ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source);

    ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise channelPromise);

    ChannelFuture unblock(InetAddress multicastAddress, InetAddress source);

    ChannelFuture unblock(InetAddress multicastAddress, InetAddress source, ChannelPromise channelPromise);

    ChannelFuture unblock(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source);

    ChannelFuture unblock(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise channelPromise);
}
