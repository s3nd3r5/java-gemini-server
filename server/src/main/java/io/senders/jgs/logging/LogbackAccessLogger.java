package io.senders.jgs.logging;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackAccessLogger implements AccessLogger {
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void in(Object... arguments) {
    if (arguments == null) {
      in("IN");
    } else {
      in("IN" + "\t{}".repeat(arguments.length), arguments);
    }
  }

  @Override
  public void in(String fmt, Object... arguments) {
    logger.info(fmt, arguments);
  }

  @Override
  public void in(String msg) {
    logger.info(msg);
  }

  @Override
  public void out(Object... arguments) {
    if (arguments == null) {
      in("OUT");
    } else {
      in("OUT" + "\t{}".repeat(arguments.length), arguments);
    }
  }

  @Override
  public void out(String fmt, Object... arguments) {
    logger.info(fmt, arguments);
  }

  @Override
  public void out(String msg) {
    logger.info(msg);
  }
}
