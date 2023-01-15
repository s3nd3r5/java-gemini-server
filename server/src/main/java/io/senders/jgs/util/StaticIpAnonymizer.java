/*-
 * -\-\-
 * Java Gemini Server
 * --
 * Copyright (C) 2021 - 2023 senders <steph (at) senders.io>
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

import java.net.SocketAddress;

/** Utility class to always return a fixed IP "0.0.0.0" */
public class StaticIpAnonymizer implements IpAnonymizer {

  private static final String STATIC_IP = "0.0.0.0";

  public StaticIpAnonymizer() {}

  /**
   * Unused. Simply returns {@link IpAnonymizer#getAnonymousIp()}
   *
   * @param _unused the incoming IP - not used here and simply ignored
   * @return {@link IpAnonymizer#getAnonymousIp()}
   */
  @Override
  public String anonymizeIp(SocketAddress _unused) {
    return getAnonymousIp();
  }

  /**
   * Returns the fixed string "0.0.0.0"
   *
   * @return a fixed IP string "0.0.0.0"
   */
  @Override
  public String getAnonymousIp() {
    return STATIC_IP;
  }
}
