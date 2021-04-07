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

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/** Utility for getting information from a file. */
public class FileUtil {
  private FileUtil() {}

  /**
   * Get the extension of the given filename
   *
   * @param filename filename to extract the extension from
   * @return the extension of the file or else empty
   */
  public static String getExtension(final String filename) {
    if (filename.lastIndexOf(".") > 0) {
      return filename.substring(filename.lastIndexOf('.') + 1);
    } else {
      return "";
    }
  }

  /**
   * Get the title from a gemtext file
   *
   * @param file gemtext file
   * @return title from the gemtext file
   */
  public static Optional<String> getTitle(final File file) {
    try {
      return Files.readAllLines(Path.of(file.getAbsolutePath()), StandardCharsets.UTF_8).stream()
          .filter(l -> l.startsWith("# "))
          .findFirst()
          .map(line -> line.substring(2))
          .map(String::trim);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
