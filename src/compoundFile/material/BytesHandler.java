package compoundFile.material;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.List;

import compoundFile.util.BytesUtil;

public class BytesHandler {
	private byte[] bytes = null;
	private ByteOrder endianType = null;
	private Charset charset = null;

	public BytesHandler(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public int getLength() {
		return bytes.length;
	}
	
	public byte[] readBytes(int offset, int length) {
		return BytesUtil.part(bytes, offset, length);
	}
	
	public long readLong(int offset, int length) {
		byte[] bytes = this.readBytes(offset, length);
		return BytesUtil.toLong(bytes, endianType);
	}
	
	public int readInt(int offset, int length) {
		byte[] bytes = this.readBytes(offset, length);
		return BytesUtil.toInteger(bytes, endianType);
	}
	
	public String readString(int offset, int length) {
		byte[] bytes = this.readBytes(offset, length);
		return BytesUtil.toString(bytes, charset);
	}
	
	public List<Integer> readIntList(int offset, int length, int splitSize) {
		byte[] bytes = this.readBytes(offset, length);
		return BytesUtil.toIntegerList(bytes, splitSize, endianType);
	}

	public void setEndianType(ByteOrder endianType) {
		this.endianType = endianType;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
}
