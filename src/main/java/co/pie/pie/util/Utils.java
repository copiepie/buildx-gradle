package co.pie.pie.util;

public class Utils {
    public static String pushOrKeepLocally(boolean pushImage) {
        if (pushImage) {
            return "--push";
        }
        String tempDir = System.getProperty("java.io.tmpdir");
        String keepLocally = String.format("-o type=tar,dest=%s%s", tempDir, "out.tar");
        return keepLocally;
    }
}
