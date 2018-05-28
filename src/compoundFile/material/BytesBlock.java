package compoundFile.material;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import compoundFile.util.BytesUtil;

public class BytesBlock {
	private byte[] bytes = null;
	private int offset;
	private int length;
	private ByteOrder endianType = ByteOrder.BIG_ENDIAN;
	private Charset charset = null;

	public BytesBlock(byte[] bytes) {
		this.bytes = bytes;
		this.offset = 0;
		this.length = bytes.length;
	}

	private BytesBlock(BytesBlock motherBlock, int offset, int length) {
		this.bytes = motherBlock.bytes;
		this.offset = offset;
		this.length = length;
		this.endianType = motherBlock.endianType;
		this.charset = motherBlock.charset;
	}

	public void write(byte[] wBytes) {
		if (length < wBytes.length) {
			throw new IndexOutOfBoundsException();
		} else if (length > wBytes.length) {
			wBytes = BytesUtil.merge(wBytes, new byte[length - wBytes.length]);
		}
		for (int i = offset; i < offset + length; i++) {
			bytes[i] = wBytes[i - offset];
		}
	}

	public byte[] read() {
		return read(0, length);
	}

	public byte[] read(int offset, int length) {
		if (rangeCheck(offset, length) == false) {
			throw new IndexOutOfBoundsException();
		}
		return BytesUtil.slice(bytes, this.offset + offset, length);
	}

	public long readLong() {
		return readLong(0, length);
	}

	public long readLong(int offset, int length) {
		byte[] bytes = this.read(offset, length);
		return BytesUtil.toLong(bytes, endianType);
	}

	public int readInt() {
		return readInt(0, length);
	}

	public int readInt(int offset, int length) {
		byte[] bytes = this.read(offset, length);
		return BytesUtil.toInteger(bytes, endianType);
	}

	public String readString() {
		return readString(0, length);
	}

	public String readString(int offset, int length) {
		byte[] bytes = this.read(offset, length);
		return BytesUtil.toString(bytes, charset);
	}

	public List<Integer> readIntList(int splitSize) {
		return readIntList(0, length, splitSize);
	}

	public List<Integer> readIntList(int offset, int length, int splitSize) {
		byte[] bytes = this.read(offset, length);
		return BytesUtil.toIntegerList(bytes, splitSize, endianType);
	}

	public BytesBlock subBlock(int offset, int length) {
		if (rangeCheck(offset, length) == false) {
			throw new IndexOutOfBoundsException();
		}
		return new BytesBlock(this, this.offset + offset, length);
	}

	public void setEndianType(ByteOrder endianType) {
		this.endianType = endianType;
		if (endianType == ByteOrder.BIG_ENDIAN) {
			this.charset = StandardCharsets.UTF_16BE;
		} else if (endianType == ByteOrder.LITTLE_ENDIAN) {
			this.charset = StandardCharsets.UTF_16LE;
		}
	}

	private boolean rangeCheck(int offset, int length) {
		return (this.length < offset + length) == false;
	}

	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}

	public ByteOrder getEndianType() {
		return endianType;
	}
}
