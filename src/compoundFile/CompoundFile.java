package compoundFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import compoundFile.component.Header;
import compoundFile.component.directory.DirectoryEntry;
import compoundFile.component.directory.DirectoryEntryTable;
import compoundFile.component.sat.MSAT;
import compoundFile.component.sat.SAT;
import compoundFile.component.sat.SSAT;
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
	private MSAT msat = null;
	private SAT sat = null;

	// stream
	private SSAT ssat = null;
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

		// sector structure
		// sectorTable
		byte[] sectorBytes = ByteHandler.part(bytes, Header.SIZE, bytes.length - Header.SIZE);
		sectorTable = new SectorTable(sectorBytes, header.getSizeOfSector());

		// sectorAllocationTable
		msat = new MSAT(sectorTable, header.getMsatBytes(), header.getEndianType());
		sat = new SAT(msat, header.getEndianType());

		// directory
		directoryEntryTable = new DirectoryEntryTable(sectorTable, sat, header.getFirstDirectoryStreamSectorId(),
				header.getSizeOfSector(), header.getEndianType(), header.getCharset());

		// short stream structure
		// shortStreamTable
		DirectoryEntry rootStorage = directoryEntryTable.get("Root Entry");
		shortStreamTable = new ShortStreamTable(sectorTable, sat, rootStorage.getFirstSectorID(),
				header.getSizeOfShortStream());

		// shortStreamAllocationTable
		ssat = new SSAT(shortStreamTable, header.getFirstSSATId(), sectorTable, sat, header.getEndianType());
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
