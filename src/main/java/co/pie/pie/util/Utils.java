package co.pie.pie.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class Utils {
  public static String pushOrKeepLocally(boolean pushImage) {
    if (pushImage) {
      return "--push";
    }
    String tempDir = System.getProperty("java.io.tmpdir");
    String keepLocally =
        String.format("-o type=tar,dest=%s%s", addTrailingFileSeparator(tempDir), "out.tar");
    return keepLocally;
  }

  public static String addTrailingFileSeparator(String dir) {
    if (!StringUtils.endsWith(dir, File.separator)) {
      return dir + File.separator;
    }
    return dir;
  }
}
