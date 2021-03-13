package io.senders.jgs.util;

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
}
