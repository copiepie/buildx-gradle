import java.io.File;

public class Test {
	public static void main(String[] args) {
		System.out.println(File.separator);
		System.out.println(System.getProperty("java.io.tmpdir") + File.separator);
	}
}
