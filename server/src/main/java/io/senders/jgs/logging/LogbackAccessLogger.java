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
