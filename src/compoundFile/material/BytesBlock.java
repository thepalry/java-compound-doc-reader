package compoundFile.material;

import java.util.List;

public class BytesBlock {
	private BytesHandler bytesHandler;
	private int offset;
	private int length;

	public BytesBlock(int offset, int length) {
		this.offset = offset;
		this.length = length;
	}

	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}

	public byte[] readBytes() {
		return bytesHandler.readBytes(offset, length);
	}

	public byte[] readBytes(int offset, int length) {
		if (rangeCheck(offset, length) == false) {
			throw new IndexOutOfBoundsException();
		}
		return bytesHandler.readBytes(this.offset + offset, length);
	}

	public long readLong() {
		return bytesHandler.readLong(offset, length);
	}

	public long readLong(int offset, int length) {
		if (rangeCheck(offset, length) == false) {
			throw new IndexOutOfBoundsException();
		}
		return bytesHandler.readLong(this.offset + offset, length);
	}

	public int readInt() {
		return bytesHandler.readInt(offset, length);
	}

	public int readInt(int offset, int length) {
		if (rangeCheck(offset, length) == false) {
			throw new IndexOutOfBoundsException();
		}
		return bytesHandler.readInt(this.offset + offset, length);
	}

	public String readString() {
		return bytesHandler.readString(offset, length);
	}

	public String readString(int offset, int length) {
		if (rangeCheck(offset, length) == false) {
			throw new IndexOutOfBoundsException();
		}
		return bytesHandler.readString(this.offset + offset, length);
	}
	
	public List<Integer> readIntList(int splitSize) {
		return bytesHandler.readIntList(offset, length, splitSize);
	}

	public BytesBlock subBlock(int offset, int length) {
		if (rangeCheck(offset, length) == false) {
			throw new IndexOutOfBoundsException();
		}
		return new BytesBlock(this.offset + offset, length);
	}

	private boolean rangeCheck(int offset, int length) {
		return (this.length < offset + length) == false;
	}

}
