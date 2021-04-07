/*-
 * -\-\-
 * Java Gemini Server
 * --
 * Copyright (C) 2021 senders <stephen (at) senders.io>
 * --
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * -/-/-
 */
package io.senders.jgs.util;

import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.SocketChannel;
import io.senders.jgs.server.Server;
import java.util.function.Function;

/**
 * Functional interface for abstracting how the channel handler is provided to the server. Used to
 * allow SNI and non-SNI SslHandlers to be used in the {@link SocketChannel} pipeline.
 *
 * @see Server#run()
 */
public @FunctionalInterface interface SslHandlerProvider
    extends Function<SocketChannel, ChannelHandler> {}
