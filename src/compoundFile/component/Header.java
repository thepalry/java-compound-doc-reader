package compoundFile.component;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import compoundFile.component.sector.Sector;
import compoundFile.material.BytesBlock;
import compoundFile.material.BytesHandler;
import compoundFile.util.BytesUtil;

/* 
Header Structure of Compound File
*/

public class Header {
	public static final int SIZE = 512;

	// identifier of special values
	private static final byte[] COMPOUND_DOC_IDENTIFIER = { -48, -49, 17, -32, -95, -79, 26, -31 }; // D0 CF 11 E0 A1 B1
																									// 1A E1
	private static final byte[] BIG_ENDIAN = { -1, -2 };
	private static final byte[] LITTLE_ENDIAN = { -2, -1 };

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
	private Charset charset = null;
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

	public Header(BytesHandler bytesHandler, BytesBlock headerBlock) throws IOException {
		this.headerBlock = headerBlock;

		// check compound file format identifier
		byte[] compoundDocIdentifier = headerBlock.readBytes(COMPOUND_DOC_IDENTIFIER_OFFSET,
				COMPOUND_DOC_IDENTIFIER_LENGTH);
		if (BytesUtil.compareBytes(COMPOUND_DOC_IDENTIFIER, compoundDocIdentifier) == false) {
			throw new IOException("Invalid file format. Invalid identifier for compound file");
		}
		byte[] endian = headerBlock.readBytes(ENDIAN_OFFSET, ENDIAN_LENGTH);
		if (BytesUtil.compareBytes(BIG_ENDIAN, endian) == true) {
			endianType = ByteOrder.BIG_ENDIAN;
			charset = StandardCharsets.UTF_16BE;
		} else if (BytesUtil.compareBytes(LITTLE_ENDIAN, endian) == true) {
			endianType = ByteOrder.LITTLE_ENDIAN;
			charset = StandardCharsets.UTF_16LE;
		} else {
			throw new IOException("Invalid file format. Invalid endian type definition.");
		}
		bytesHandler.setCharset(charset);
		bytesHandler.setEndianType(endianType);

		uid = headerBlock.readBytes(UID_OFFSET, UID_LENGTH);
		revision = headerBlock.readBytes(REVISION_NUM_OFFSET, REVISION_NUM_LENGTH);
		version = headerBlock.readBytes(VERSION_NUM_OFFSET, VERSION_NUM_LENGTH);

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

	public Charset getCharset() {
		return charset;
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
}