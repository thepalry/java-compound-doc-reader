package compoundFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import compoundFile.component.Header;
import compoundFile.component.directory.DirectoryEntry;
import compoundFile.component.directory.DirectoryEntryTable;
import compoundFile.component.sector.Sector;
import compoundFile.component.sector.SectorTable;
import compoundFile.component.shortStream.ShortStreamTable;
import compoundFile.util.ByteHandler;

/*
	Handler for Microsoft Compound File Binary Format(Microsoft Compound Document File Format, CFBF)
	writer : Huijae Jeong (thepalry@naver.com; github.com/thepalry)
	
	****** Note !! ******
	This handler do NOT change structure of document.
	This changes internal data ONLY. (based on directory)
*/

public class CompoundFile {
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

		// header
		byte[] headerBytes = ByteHandler.part(bytes, 0, Header.SIZE);
		header = new Header(headerBytes);
		ByteOrder endianType = header.getEndianType();
		Charset charset = header.getCharset();

		// sectorTable
		byte[] sectorBytes = ByteHandler.part(bytes, Header.SIZE, bytes.length - Header.SIZE);
		byte[] msatBytes = header.getMsatBytes();
		int sizeOfSector = header.getSizeOfSector();
		sectorTable = new SectorTable(msatBytes, sectorBytes, sizeOfSector, endianType);

		// directory
		int firstDirectoryStreamSectorID = header.getFirstDirectoryStreamSectorId();
		directoryEntryTable = new DirectoryEntryTable(sectorTable, firstDirectoryStreamSectorID, sizeOfSector,
				endianType, charset);

		// shortStreamTable
		DirectoryEntry rootStorage = directoryEntryTable.get(DirectoryEntryTable.ROOT_ENTRY);
		int firstShortStreamSectorID = rootStorage.getFirstSectorID();
		int sizeOfShortStream = header.getSizeOfShortStream();
		int firstSSATID = header.getFirstSSATId();
		shortStreamTable = new ShortStreamTable(sectorTable, firstShortStreamSectorID, sizeOfShortStream, firstSSATID,
				endianType);
	}

	public void write(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);

		fos.write(header.getBytes());
		for (Sector sector : sectorTable) {
			fos.write(sector.getBytes());
		}
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
