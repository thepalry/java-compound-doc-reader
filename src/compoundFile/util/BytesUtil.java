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
			byte[] splitedByte = part(bytes, splitSize * i, splitSize);
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
			return (int) ByteBuffer.wrap(bytes).order(endianType).getShort();
		}
		return ByteBuffer.wrap(bytes).order(endianType).getInt();
	}

	public static byte[] part(byte[] bytes, int offset, int length) {
		return Arrays.copyOfRange(bytes, offset, offset + length);
	}

	public static byte[] merge(byte[] bArr1, byte[] bArr2) {
		byte[] result = Arrays.copyOf(bArr1, bArr1.length + bArr2.length);
		System.arraycopy(bArr2, 0, result, bArr1.length, bArr2.length);
		return result;
	}

	public static boolean compareBytes(byte[] a, byte[] b) {
		if (a.length != b.length) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}

	public static String byteToBitstring(byte b) {
		StringBuilder sb = new StringBuilder();
		int byteToInt = b & 0xff;
		for (int i = 0; i < Byte.SIZE; i++) {
			sb.append(byteToInt % 2);
			byteToInt /= 2;
		}
		return sb.reverse().toString();
	}

	public static Byte bitstringToByte(String bitString) {
		Byte result = 0;
		for (char c : bitString.toCharArray()) {
			switch (c) {
			case '0':
			case '1':
				result = (byte) (result * 2 + (int) (c - '0'));
				break;
			default:
				return null;
			}
		}
		return result;
	}
}