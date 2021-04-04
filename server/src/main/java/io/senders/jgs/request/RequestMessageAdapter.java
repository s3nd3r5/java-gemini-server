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
package io.senders.jgs.request;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.senders.jgs.exceptions.ServerBaseException;
import io.senders.jgs.logging.AccessLogger;
import io.senders.jgs.logging.LogbackAccessLogger;
import io.senders.jgs.request.handlers.NotFoundHandler;
import io.senders.jgs.request.handlers.RequestHandler;
import io.senders.jgs.request.routers.Host;
import io.senders.jgs.response.ResponseMessage;
import io.senders.jgs.status.GeminiStatus;
import io.senders.jgs.util.RouteMatcher;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestMessageAdapter extends ChannelInboundHandlerAdapter {
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final AccessLogger accessLogger = new LogbackAccessLogger();
  private final Collection<Host> hosts;
  private final RequestHandler fallbackRouteHandler;

  public RequestMessageAdapter(Host... hosts) {
    this.hosts = Arrays.asList(hosts);
    this.fallbackRouteHandler = new NotFoundHandler();
  }

  public RequestMessageAdapter(RequestHandler fallbackRouteHandler, Host... hosts) {
    this.fallbackRouteHandler = fallbackRouteHandler;
    this.hosts = Arrays.asList(hosts);
  }

  public RequestMessageAdapter(final RequestHandler fallbackRouteHandler, Collection<Host> hosts) {
    this.fallbackRouteHandler = fallbackRouteHandler;
    this.hosts = hosts;
  }

  @Override
  public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
    try {
      ByteBuf buffer = (ByteBuf) msg;
      StringBuilder urlBuffer = new StringBuilder(1024);
      int bytesRead = 0;
      while (buffer.isReadable()) {
        char datum = (char) buffer.readByte();
        if (datum == '\r') {
          if (buffer.readByte() != '\n') {
            throw new RuntimeException("Invalid request scheme");
          }
        } else {
          urlBuffer.append(datum);
          bytesRead++;
        }
        if (bytesRead == 1024) {
          if (buffer.readByte() == '\r') {
            bytesRead++;
            if (buffer.readByte() != '\n') {
              throw new RuntimeException("Invalid request scheme");
            }
            bytesRead++;
          } else {
            throw new RuntimeException("Invalid request scheme");
          }
        }
      }
      String url = urlBuffer.toString();
      accessLogger.in(ctx.channel().remoteAddress(), url, bytesRead);

      final Request request = new Request(URI.create(url));

      ResponseMessage response =
          hosts.stream()
              .filter(h -> h.hosts().equals(request.uri().getHost()))
              .flatMap(h -> h.routers().stream())
              .flatMap(r -> r.routes().stream())
              .filter(r -> RouteMatcher.match(r.route(), request.uri()))
              .findFirst()
              .map(r -> r.handle(request))
              .orElse(this.fallbackRouteHandler.handle(request));

      byte[] data = response.toBytes();
      ByteBuf res = ctx.alloc().buffer(data.length);
      res.writeBytes(data);

      accessLogger.out(response.status(), response.meta(), data.length);

      final ChannelFuture f = ctx.writeAndFlush(res);
      f.addListener(
          future -> {
            assert f == future;
            ctx.close();
          });
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    logger.debug("Caught exception", cause);
    final ResponseMessage msg;
    if (cause instanceof ServerBaseException) {
      msg = new ResponseMessage(((ServerBaseException) cause).getStatus(), cause.getMessage());
    } else {
      msg = new ResponseMessage(GeminiStatus.PERMANENT_FAILURE, cause.getMessage());
      logger.error("Unexpected exception", cause);
    }
    byte[] data = msg.toBytes();
    ByteBuf res = ctx.alloc().buffer(data.length);
    res.writeBytes(data);

    accessLogger.out(msg.status(), msg.meta(), data.length);

    ctx.writeAndFlush(res);
    ctx.close();
  }
}
