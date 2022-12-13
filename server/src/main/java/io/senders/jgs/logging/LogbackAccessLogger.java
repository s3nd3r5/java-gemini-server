/*-
 * -\-\-
 * Java Gemini Server
 * --
 * Copyright (C) 2021 - 2022 senders <steph (at) senders.io>
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
package io.senders.jgs.logging;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logback implementation of the AccessLogger. Uses the SLF4J logback implementation and is
 * configured in the root logback.xml to write to a separate file than the standard loggers in each
 * class. Can be configured by specifying the logger in your logback.xml using the * fully qualified
 * classname: io.senders.jgs.logging.LogbackAccessLogger
 *
 * @see Logger
 */
public class LogbackAccessLogger implements AccessLogger {

  /** Logger implementation. */
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Logs incoming request information
   *
   * @param arguments request info to log
   * @implNote uses TSV format IN\t{}\t{} etc
   */
  @Override
  public void in(Object... arguments) {
    if (arguments == null) {
      log("IN");
    } else {
      log("IN" + "\t{}".repeat(arguments.length), arguments);
    }
  }

  /**
   * Logs outgoing response information
   *
   * @param arguments response info to log
   * @implNote uses TSV format OUT\t{}\t{} etc
   */
  @Override
  public void out(Object... arguments) {
    if (arguments == null) {
      log("OUT");
    } else {
      log("OUT" + "\t{}".repeat(arguments.length), arguments);
    }
  }

  /**
   * Wraps slf4j info log API
   *
   * @param fmt message format for log
   * @param arguments info to log
   * @see Logger#info(String, Object...)
   */
  @Override
  public void log(String fmt, Object... arguments) {
    logger.info(fmt, arguments);
  }

  /**
   * Wraps slf4j info log API
   *
   * @param msg message to log
   * @see Logger#info(String)
   */
  @Override
  public void log(String msg) {
    logger.info(msg);
  }
}
