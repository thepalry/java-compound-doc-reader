package compoundFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import compoundFile.component.Header;
import compoundFile.component.directory.DirectoryEntry;
import compoundFile.component.directory.DirectoryEntryTable;
import compoundFile.component.sector.SectorTable;
import compoundFile.component.shortStream.ShortStreamTable;
import compoundFile.material.BytesBlock;

/*
	Handler for Microsoft Compound File Binary Format(Microsoft Compound Document File Format, CFBF)
	writer : Huijae Jeong (thepalry@naver.com; github.com/thepalry)
	
	****** Note !! ******
	This handler do NOT change structure of document.
	This changes internal data ONLY. (based on directory)
*/

public class CompoundFile {
	// file bytes
	private BytesBlock totalBytes = null;

	// header
	private Header header = null;

	// sector
	private SectorTable sectorTable = null;

	// stream
	private ShortStreamTable shortStreamTable = null;

	// directory
	private DirectoryEntryTable directoryEntryTable = null;

	public CompoundFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] bytes = new byte[fis.available()];
		fis.read(bytes);
		fis.close();

		totalBytes = new BytesBlock(bytes);

		// header
		BytesBlock headerBlock = totalBytes.subBlock(0, Header.SIZE);
		ByteOrder endianType = Header.getEndianType(headerBlock);
		if (Header.isCompoundFile(headerBlock) == false || endianType == null) {
			throw new IOException("Invalid Compound file format");
		}
		totalBytes.setEndianType(endianType);
		headerBlock.setEndianType(endianType);

		header = new Header(headerBlock);
	
		// sectorTable
		BytesBlock sectorBlock = totalBytes.subBlock(Header.SIZE, bytes.length - Header.SIZE);
		BytesBlock msatBytes = header.getMSATBlock();
		int sizeOfSector = header.getSizeOfSector();
		sectorTable = new SectorTable(msatBytes, sectorBlock, sizeOfSector);

		// directory
		int firstDirectoryStreamSectorID = header.getFirstDirectoryStreamSectorId();
		directoryEntryTable = new DirectoryEntryTable(sectorTable, firstDirectoryStreamSectorID);

		// shortStreamTable
		DirectoryEntry rootStorage = directoryEntryTable.get(DirectoryEntryTable.ROOT_ENTRY);
		int firstShortStreamSectorID = rootStorage.getFirstSectorID();
		int sizeOfShortStream = header.getSizeOfShortStream();
		int firstSSATID = header.getFirstSSATId();
		shortStreamTable = new ShortStreamTable(sectorTable, firstShortStreamSectorID, sizeOfShortStream, firstSSATID);
	}

	public void write(File file) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(totalBytes.readBytes(0, totalBytes.getLength()));
			fos.flush();
		}
	}

	public byte[] getUid() {
		return header.getUid();
	}

	public byte[] getRevision() {
		return header.getRevision();
	}

	public byte[] getVersion() {
		return header.getVersion();
	}
}
