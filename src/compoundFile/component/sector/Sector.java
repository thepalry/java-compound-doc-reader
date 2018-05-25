package compoundFile.component.sector;

import compoundFile.material.BytesBlock;

public class Sector {
	public static final int ID_LENGTH = 4;
	public static final int FREE_SECTOR_ID = -1;
	public static final int END_OF_CHAIN = -2;
	public static final int SECTOR_ALLOCATION_TABLE = -3;
	public static final int MASTER_SECTOR_ALLOCATION_TABLE = -4;

	private int id;
	private BytesBlock block;

	public Sector(int id, BytesBlock block) {
		this.id = id;
		this.block = block;
	}

	public int getID() {
		return id;
	}
	
	public BytesBlock getBlock() {
		return block;
	}

	public static boolean isSpecialID(int sectorID) {
		return sectorID < 0;
	}
}
