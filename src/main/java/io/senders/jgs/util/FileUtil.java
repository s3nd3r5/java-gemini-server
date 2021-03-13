package io.senders.jgs.util;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class FileUtil {
  private FileUtil() {}

  /**
   * Give a filename return its extension
   *
   * @param filename the fullname of the file (no path)
   * @return string of the extension or empty string if non exists
   * @implNote dotfiles like .bash_profile will return their name: bash_profile
   * @implNote This expects the name to be a file - if its a nested filepath like /file.zip/data you
   *     will get unexpected results. For best performance access the filename through the nio api.
   */
  public static String getExtension(final String filename) {
    if (filename.lastIndexOf(".") > 0) {
      return filename.substring(filename.lastIndexOf('.') + 1);
    } else {
      return "";
    }
  }

  /**
   * Parse the title out of a file
   *
   * @param file file to read
   * @return file's title
   * @throws UncheckedIOException to be handled by the caller
   */
  public static Optional<String> getTitle(final File file) throws UncheckedIOException {
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
