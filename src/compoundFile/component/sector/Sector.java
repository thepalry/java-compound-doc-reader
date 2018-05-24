package compoundFile.component.sector;

import compoundFile.util.ByteHandler;

public class Sector {
	public static final int ID_LENGTH = 4;
	public static final int FREE_SECTOR_ID = -1;
	public static final int END_OF_CHAIN = -2;
	public static final int SECTOR_ALLOCATION_TABLE = -3;
	public static final int MASTER_SECTOR_ALLOCATION_TABLE = -4;

	private int id;
	private byte[] bytes;
	private int size;

	public Sector(int id, byte[] bytes) {
		this.id = id;
		this.bytes = bytes;
		this.size = bytes.length;
	}

	public void setBytes(byte[] bytes) {
		if (bytes.length > size) {
			bytes = ByteHandler.part(bytes, 0, size);
		} else if (bytes.length < size) {
			bytes = ByteHandler.merge(bytes, new byte[size - bytes.length]);
		}
		this.bytes = bytes;
	}

	public int getID() {
		return id;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public static boolean isSpecialID(int sectorID) {
		return sectorID < 0;
	}
}
