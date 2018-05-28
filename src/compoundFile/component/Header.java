package compoundFile.component;

import java.io.IOException;
import java.nio.ByteOrder;

import compoundFile.component.sector.Sector;
import compoundFile.material.BytesBlock;

/* 
Header Structure of Compound File
*/

public class Header {
	public static final int SIZE = 512;

	// identifier of special values
	private static final long COMPOUND_DOC_IDENTIFIER = -3400479537158350111l; // 0xD0CF11E0A1B11AE1
	private static final int BIG_ENDIAN = -2; // 0xFFFE
	private static final int LITTLE_ENDIAN = -257; // 0xFEFF

	private static final int COMPOUND_DOC_IDENTIFIER_OFFSET = 0;
	private static final int COMPOUND_DOC_IDENTIFIER_LENGTH = 8;

	private static final int UID_OFFSET = 8;
	private static final int UID_LENGTH = 16;

	private static final int REVISION_NUM_OFFSET = 24;
	private static final int REVISION_NUM_LENGTH = 2;

	private static final int VERSION_NUM_OFFSET = 26;
	private static final int VERSION_NUM_LENGTH = 2;

	private static final int ENDIAN_OFFSET = 28;
	private static final int ENDIAN_LENGTH = 2;

	private static final int SECTOR_SIZE_OFFSET = 30;
	private static final int SECTOR_SIZE_LENGTH = 2;

	private static final int SHORT_STREAM_SIZE_OFFSET = 32;
	private static final int SHORT_STREAM_SIZE_LENGTH = 2;

	private static final int NO_SAT_OFFSET = 44;
	private static final int NO_SAT_LENGTH = 4;

	private static final int FIRST_DIRECTORY_STREAM_ID_OFFSET = 48;
	private static final int FIRST_SSAT_ID_OFFSET = 60;
	private static final int FIRST_MSAT_ID_OFFSET = 68;

	private static final int MIN_SIZE_OF_STANDARD_STREAM_OFFSET = 56;
	private static final int MIN_SIZE_OF_STANDARD_STREAM_LENGTH = 4;

	private static final int NO_SSAT_OFFSET = 64;
	private static final int NO_SSAT_LENGTH = 4;

	private static final int NO_MSAT_OFFSET = 72;
	private static final int NO_MSAT_LENGTH = 4;

	private static final int MASTER_SECTOR_ID_IN_HEADER_OFFSET = 76;
	private static final int MASTER_SECTOR_ID_IN_HEADER_LENGTH = 436;

	private BytesBlock headerBlock;

	private byte[] uid = null;
	private byte[] revision = null;
	private byte[] version = null;
	private ByteOrder endianType = null;
	private int sizeOfSector = 0;
	private int sizeOfShortStream = 0;
	private int noSAT = 0; // no Sector allocation table
	private int firstDirectoryStreamSectorId = 0;
	private int minSizeOfStandardStream = 0;
	private int firstSSATId = 0; // first sector id of short-stream allocation table
	private int noSSAT = 0; // no short-stream allocation table
	private int firstMSATId = 0; // first sector id of the master sector allocation table id
	private int noMSAT = 0; // no master sector allocation table
	private BytesBlock msatBlock;

	public Header(BytesBlock headerBlock) throws IOException {
		this.headerBlock = headerBlock;
		
		this.endianType = headerBlock.getEndianType();

		uid = headerBlock.read(UID_OFFSET, UID_LENGTH);
		revision = headerBlock.read(REVISION_NUM_OFFSET, REVISION_NUM_LENGTH);
		version = headerBlock.read(VERSION_NUM_OFFSET, VERSION_NUM_LENGTH);

		int sizeOfSectorRaw = headerBlock.readInt(SECTOR_SIZE_OFFSET, SECTOR_SIZE_LENGTH);
		sizeOfSector = (int) Math.pow(2, sizeOfSectorRaw);

		int sizeOfShortSectorRaw = headerBlock.readInt(SHORT_STREAM_SIZE_OFFSET, SHORT_STREAM_SIZE_LENGTH);
		sizeOfShortStream = (int) Math.pow(2, sizeOfShortSectorRaw);

		noSAT = headerBlock.readInt(NO_SAT_OFFSET, NO_SAT_LENGTH);
		firstDirectoryStreamSectorId = headerBlock.readInt(FIRST_DIRECTORY_STREAM_ID_OFFSET, Sector.ID_LENGTH);
		minSizeOfStandardStream = headerBlock.readInt(MIN_SIZE_OF_STANDARD_STREAM_OFFSET,
				MIN_SIZE_OF_STANDARD_STREAM_LENGTH);
		firstSSATId = headerBlock.readInt(FIRST_SSAT_ID_OFFSET, Sector.ID_LENGTH);
		noSSAT = headerBlock.readInt(NO_SSAT_OFFSET, NO_SSAT_LENGTH);
		firstMSATId = headerBlock.readInt(FIRST_MSAT_ID_OFFSET, Sector.ID_LENGTH);
		noMSAT = headerBlock.readInt(NO_MSAT_OFFSET, NO_MSAT_LENGTH);

		msatBlock = headerBlock.subBlock(MASTER_SECTOR_ID_IN_HEADER_OFFSET, MASTER_SECTOR_ID_IN_HEADER_LENGTH);
	}

	public byte[] getUid() {
		return uid;
	}

	public byte[] getRevision() {
		return revision;
	}

	public byte[] getVersion() {
		return version;
	}

	public ByteOrder getEndianType() {
		return endianType;
	}

	public int getSizeOfSector() {
		return sizeOfSector;
	}

	public int getSizeOfShortStream() {
		return sizeOfShortStream;
	}

	public int getNoSAT() {
		return noSAT;
	}

	public int getFirstDirectoryStreamSectorId() {
		return firstDirectoryStreamSectorId;
	}

	public int getMinSizeOfStandardStream() {
		return minSizeOfStandardStream;
	}

	public int getFirstSSATId() {
		return firstSSATId;
	}

	public int getNoSSAT() {
		return noSSAT;
	}

	public int getFirstMSATId() {
		return firstMSATId;
	}

	public int getNoMSAT() {
		return noMSAT;
	}

	public BytesBlock getMSATBlock() {
		return msatBlock;
	}

	public static boolean isCompoundFile(BytesBlock headerBlock) {
		long compoundDocIdentifier = headerBlock.readLong(COMPOUND_DOC_IDENTIFIER_OFFSET,
				COMPOUND_DOC_IDENTIFIER_LENGTH);
		return compoundDocIdentifier == COMPOUND_DOC_IDENTIFIER;
	}

	public static ByteOrder getEndianType(BytesBlock headerBlock) {
		ByteOrder endianType = null;
		int endian = headerBlock.readInt(ENDIAN_OFFSET, ENDIAN_LENGTH);
		switch (endian) {
		case BIG_ENDIAN:
			endianType = ByteOrder.BIG_ENDIAN;
			break;
		case LITTLE_ENDIAN:
			endianType = ByteOrder.LITTLE_ENDIAN;
			break;
		}
		return endianType;
	}
}