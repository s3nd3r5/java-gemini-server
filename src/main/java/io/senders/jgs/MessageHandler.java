package io.senders.jgs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.senders.jgs.exceptions.ServerBaseException;
import io.senders.jgs.response.ResponseDoc;
import io.senders.jgs.response.ResponseMessage;
import io.senders.jgs.status.GeminiStatus;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler extends ChannelInboundHandlerAdapter {
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final FileManager fileManager;

  public MessageHandler(FileManager fileManager) {
    this.fileManager = fileManager;
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
            if (buffer.readByte() != '\n') {
              throw new RuntimeException("Invalid request scheme");
            }
          } else {
            throw new RuntimeException("Invalid request scheme");
          }
        }
      }
      String url = urlBuffer.toString();
      logger.info("Received URL {} of {} bytes", url, bytesRead);

      URI uri = URI.create(url);

      ResponseDoc responseDoc = fileManager.load(uri);

      byte[] data = responseDoc.toResponseMessage();
      ByteBuf res = ctx.alloc().buffer(data.length);
      res.writeBytes(data);
      logger.info("Served: {}", new String(data));

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
    logger.info("Served: {}", new String(data));

    ctx.writeAndFlush(res);
    ctx.close();
  }
}
