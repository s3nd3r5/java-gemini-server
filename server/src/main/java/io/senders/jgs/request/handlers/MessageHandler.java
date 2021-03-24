package io.senders.jgs.request.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.senders.jgs.exceptions.ServerBaseException;
import io.senders.jgs.request.Request;
import io.senders.jgs.request.Router;
import io.senders.jgs.response.ResponseMessage;
import io.senders.jgs.status.GeminiStatus;
import io.senders.jgs.util.RouteMatcher;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler extends ChannelInboundHandlerAdapter {
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final Stream<Router> routers;
  private final RouteHandler defaultHandler;

  public MessageHandler(Router...  routers) {
    this.routers = Stream.of(routers);
    this.defaultHandler = new NotFoundHandler();
  }

  public MessageHandler(RouteHandler defaultHandler, Router... routers) {
    this.routers = Stream.of(routers);
    this.defaultHandler = defaultHandler;
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
      logger.info("IN\t{}\t{}", url, bytesRead);

      final Request request = new Request(URI.create(url));

      ResponseMessage response = routers.flatMap(Router::routes)
          .filter(r -> RouteMatcher.match(r.route(), request.uri()))
          .findFirst()
          .map(r -> r.handle(request))
          .orElse(this.defaultHandler.handle(request.uri()));

      byte[] data = response.toResponseMessage();
      ByteBuf res = ctx.alloc().buffer(data.length);
      res.writeBytes(data);

      logger.info("OUT\t{}\t{}\t{}", response.status(), response.meta(), data.length);

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
    logger.error("Caught exception", cause);
    final ResponseMessage msg;
    if (cause instanceof ServerBaseException) {
      msg = new ResponseMessage(((ServerBaseException) cause).getStatus(), cause.getMessage());
    } else {
      msg = new ResponseMessage(GeminiStatus.PERMANENT_FAILURE, cause.getMessage());
    }

    byte[] data = msg.toResponseMessage();
    ByteBuf res = ctx.alloc().buffer(data.length);
    res.writeBytes(data);

    ctx.writeAndFlush(res);
    ctx.close();
  }
}
