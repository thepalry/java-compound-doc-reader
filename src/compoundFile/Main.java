package compoundFile;

import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		/*
		 * if (args == null || args.length <= 0 || args[0] == null) {
		 * System.out.println("type file path"); return; } String filename = args[0];
		 */
		CompoundFile compoundFile1 = new CompoundFile(new File("C://한글문서파일형식_5.0_revision1.2.hwp"));
		compoundFile1.write(new File("C://Users/HJ/Desktop/hwptest.hwp"));

		// CompoundFile compoundFile2 = new CompoundFile(new
		// File("C://hwp5.0sample.hwp"));
	}
}
