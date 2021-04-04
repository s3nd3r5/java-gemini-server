package io.senders.jgs.logging;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Logback based implementation of the AccessLogger. */
public class LogbackAccessLogger implements AccessLogger {
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void in(Object... arguments) {
    if (arguments == null) {
      log("IN");
    } else {
      log("IN" + "\t{}".repeat(arguments.length), arguments);
    }
  }

  @Override
  public void out(Object... arguments) {
    if (arguments == null) {
      log("OUT");
    } else {
      log("OUT" + "\t{}".repeat(arguments.length), arguments);
    }
  }

  @Override
  public void log(String fmt, Object... arguments) {
    logger.info(fmt, arguments);
  }

  @Override
  public void log(String msg) {
    logger.info(msg);
  }
}
