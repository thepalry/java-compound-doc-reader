package compoundFile.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
	Common method for handling byte array of Compound File.
*/

public class BytesUtil {

	public static List<Integer> toIntegerList(byte[] bytes, int splitSize, ByteOrder endianType) {
		List<Integer> intList = new ArrayList<Integer>();
		for (int i = 0; i < bytes.length / splitSize; i++) {
			byte[] splitedByte = slice(bytes, splitSize * i, splitSize);
			intList.add(toInteger(splitedByte, endianType));
		}
		return intList;
	}

	public static String toString(byte[] bytes, Charset charset) {
		return new String(bytes, charset);
	}

	public static long toLong(byte[] bytes, ByteOrder endianType) {
		if (bytes.length < Long.BYTES) {
			return (long) toInteger(bytes, endianType);
		}
		return ByteBuffer.wrap(bytes).order(endianType).getLong();
	}

	public static int toInteger(byte[] bytes, ByteOrder endianType) {
		if (bytes.length < Integer.BYTES) {
			return (int) toShort(bytes, endianType);
		}
		return ByteBuffer.wrap(bytes).order(endianType).getInt();
	}

	public static short toShort(byte[] bytes, ByteOrder endianType) {
		if (bytes.length < Short.BYTES) {
			return (short) bytes[0];
		}
		return ByteBuffer.wrap(bytes).order(endianType).getShort();
	}

	public static byte[] slice(byte[] bytes, int offset, int length) {
		return Arrays.copyOfRange(bytes, offset, offset + length);
	}

	public static byte[] merge(byte[] bArr1, byte[] bArr2) {
		byte[] result = Arrays.copyOf(bArr1, bArr1.length + bArr2.length);
		System.arraycopy(bArr2, 0, result, bArr1.length, bArr2.length);
		return result;
	}
}