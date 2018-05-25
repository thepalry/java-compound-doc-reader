package compoundFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import compoundFile.component.Header;
import compoundFile.component.directory.DirectoryEntry;
import compoundFile.component.directory.DirectoryEntryTable;
import compoundFile.component.sector.SectorTable;
import compoundFile.component.shortStream.ShortStreamTable;
import compoundFile.material.BytesBlock;
import compoundFile.material.BytesHandler;

/*
	Handler for Microsoft Compound File Binary Format(Microsoft Compound Document File Format, CFBF)
	writer : Huijae Jeong (thepalry@naver.com; github.com/thepalry)
	
	****** Note !! ******
	This handler do NOT change structure of document.
	This changes internal data ONLY. (based on directory)
*/

public class CompoundFile {
	// physical form itself
	private BytesHandler bytesHandler = null;

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

		bytesHandler = new BytesHandler(bytes);

		// header
		BytesBlock headerBlock = new BytesBlock(0, Header.SIZE);
		header = new Header(bytesHandler, headerBlock);
		// header 중에서도 top level info (endian type 같은) 거 먼저 뽑고 다음 bytes handler에 지정한다음
		// 나머지 뽑는 거로 분리

		// sectorTable
		BytesBlock sectorBlock = new BytesBlock(Header.SIZE, bytes.length - Header.SIZE);
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
		FileOutputStream fos = new FileOutputStream(file);

		fos.write(bytesHandler.readBytes(0, bytesHandler.getLength()));
		fos.flush();

		fos.close();
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
