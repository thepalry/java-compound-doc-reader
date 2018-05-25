package compoundFile.component.directory;

import compoundFile.component.sector.Sector;
import compoundFile.component.sector.SectorTable;
import compoundFile.component.shortStream.ShortStream;
import compoundFile.component.shortStream.ShortStreamTable;
import compoundFile.material.BytesBlock;
import compoundFile.util.BytesUtil;
import compoundFile.util.TimeStamp;

public class DirectoryEntry {
	public static final int SIZE = 128;

	private static final int NAME_LENGTH = 64;
	private static final int NAME_AREA_LENGTH = 2;
	private static final int TYPE_LENGTH = 1;
	private static final int COLOR_LENGTH = 1;
	private static final int ID_LENGTH = 4;
	private static final int UNIQUE_ID_LENGTH = 16;
	private static final int USER_FLAGS_LENGTH = 4;
	private static final int TIMESTAMP_LENGTH = 8;
	private static final int FIRST_SECTOR_LENGTH = 4;
	private static final int TOTAL_STREAM_SIZE_LENGTH = 4;

	private static final int NAME_OFFSET = 0;
	private static final int NAME_AREA_OFFSET = 64;
	private static final int TYPE_OFFSET = 66;
	private static final int COLOR_OFFSET = 67;
	private static final int LEFT_CHILD_ID_OFFSET = 68;
	private static final int RIGHT_CHILD_ID_OFFSET = 72;
	private static final int ROOT_ID_OFFSET = 76;
	private static final int UNIQUE_ID_OFFSET = 80;
	private static final int USER_FLAGS_OFFSET = 96;
	private static final int CREATED_TIME_STAMP_OFFSET = 100;
	private static final int LAST_MODIFIED_TIME_STAMP_OFFSET = 108;
	private static final int FIRST_SECTOR_OFFSET = 116;
	private static final int TOTAL_STREAM_SIZE_OFFSET = 120;

	private String name;
	private int nameLength;
	private DirectoryEntryType type;
	private int color;
	private int leftChildDirID;
	private int rightChildDirID;
	private int rootDirID;
	private byte[] uniqueID;
	private byte[] userFlags;
	private TimeStamp createdTime;
	private TimeStamp modifiedTime;
	private int firstSectorID;
	private int totalStreamSize;

	public DirectoryEntry(BytesBlock entryBlock) {
		nameLength = entryBlock.readInt(NAME_AREA_OFFSET, NAME_AREA_LENGTH);

		if (nameLength > 0) {
			// remove trailing zero
			name = entryBlock.readString(NAME_OFFSET, NAME_LENGTH).substring(0, nameLength / Character.BYTES - 1);
		}

		int typeInt = entryBlock.readInt(TYPE_OFFSET, TYPE_LENGTH);
		type = DirectoryEntryType.valueOf(typeInt);

		color = entryBlock.readInt(COLOR_OFFSET, COLOR_LENGTH);

		leftChildDirID = entryBlock.readInt(LEFT_CHILD_ID_OFFSET, ID_LENGTH);
		rightChildDirID = entryBlock.readInt(RIGHT_CHILD_ID_OFFSET, ID_LENGTH);
		rootDirID = entryBlock.readInt(ROOT_ID_OFFSET, ID_LENGTH);

		uniqueID = entryBlock.readBytes(UNIQUE_ID_OFFSET, UNIQUE_ID_LENGTH);
		userFlags = entryBlock.readBytes(USER_FLAGS_OFFSET, USER_FLAGS_LENGTH);

		long createdTimeLong = entryBlock.readLong(CREATED_TIME_STAMP_OFFSET, TIMESTAMP_LENGTH);
		createdTime = new TimeStamp(createdTimeLong);

		long modifiedTimeLong = entryBlock.readLong(LAST_MODIFIED_TIME_STAMP_OFFSET, TIMESTAMP_LENGTH);
		modifiedTime = new TimeStamp(modifiedTimeLong);

		firstSectorID = entryBlock.readInt(FIRST_SECTOR_OFFSET, FIRST_SECTOR_LENGTH);
		totalStreamSize = entryBlock.readInt(TOTAL_STREAM_SIZE_OFFSET, TOTAL_STREAM_SIZE_LENGTH);
	}

	public byte[] getData(SectorTable sectorTable, ShortStreamTable shortStreamTable, int minSizeOfStandardStream) {
		byte[] data = new byte[0];
		if (totalStreamSize >= minSizeOfStandardStream) {
			Sector sector = sectorTable.get(firstSectorID);
			while (sector != null) {
				data = BytesUtil.merge(data, sector.getBlock().readBytes());
				sector = sectorTable.getNext(sector);
			}
		} else {
			ShortStream shortStream = shortStreamTable.get(firstSectorID);
			while (shortStream != null) {
				data = BytesUtil.merge(data, shortStream.getBlock().readBytes());
				shortStream = shortStreamTable.getNext(shortStream);
			}
		}
		return data;
	}

	public void setData(SectorTable sectorTable, ShortStreamTable shortStreamTable, int minSizeOfStandardStream,
			byte[] setData) {
		if (totalStreamSize >= minSizeOfStandardStream) {
			Sector sector = sectorTable.get(firstSectorID);
			int sectorSize = sectorTable.getSizeOfSector();
			for (int i = 0; i < (int) Math.ceil(setData.length / sectorSize); i++) {
				byte[] newData = BytesUtil.part(setData, i * sectorSize, sectorSize);
				//sector.setBytes(newData);
				sector = sectorTable.getNext(sector);
			}
		} else {
			ShortStream shortStream = shortStreamTable.get(firstSectorID);
			int shortStreamSize = shortStreamTable.getSizeOfShortStream();
			for (int i = 0; i < (int) Math.ceil(setData.length / shortStreamSize); i++) {
				byte[] newData = BytesUtil.part(setData, i * shortStreamSize, shortStreamSize);
				//shortStream.setBytes(newData);
				shortStream = shortStreamTable.getNext(shortStream);
			}
		}
	}

	public String getName() {
		return name;
	}

	public DirectoryEntryType getType() {
		return type;
	}

	public int getColor() {
		return color;
	}

	public int getLeftChildDirID() {
		return leftChildDirID;
	}

	public int getRightChildDirID() {
		return rightChildDirID;
	}

	public int getRootDirID() {
		return rootDirID;
	}

	public byte[] getUniqueID() {
		return uniqueID;
	}

	public byte[] getUserFlags() {
		return userFlags;
	}

	public TimeStamp getCreatedTime() {
		return createdTime;
	}

	public TimeStamp getModifiedTime() {
		return modifiedTime;
	}

	public int getFirstSectorID() {
		return firstSectorID;
	}

	public int getSize() {
		return totalStreamSize;
	}
}