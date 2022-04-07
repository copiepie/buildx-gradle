package io.github.perpk.buildx.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class Utils {
  public static String pushOrKeepLocally(boolean pushImage, String tarFilename) {
    if (pushImage) {
      return "--push";
    }
    String keepLocally =
        String.format("-o type=tar,dest=%s", prependTmpDirToFilename(tarFilename));
    return keepLocally;
  }

  public static String prependTmpDirToFilename(String filename) {
    return String.format(
        "%s%s",
        addTrailingFileSeparator(System.getProperty("java.io.tmpdir")), filename);
  }

  public static String addTrailingFileSeparator(String dir) {
    if (!StringUtils.endsWith(dir, File.separator)) {
      return dir + File.separator;
    }
    return dir;
  }
}
