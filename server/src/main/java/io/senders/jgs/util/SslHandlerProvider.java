package io.senders.jgs.util;

import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.SocketChannel;
import java.util.function.Function;

public @FunctionalInterface interface SslHandlerProvider
    extends Function<SocketChannel, ChannelHandler> {}
