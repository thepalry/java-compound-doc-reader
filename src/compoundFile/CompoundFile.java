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

	// basic info of file
	private byte[] uid = null;
	private byte[] revision = null;
	private byte[] version = null;
	private ByteOrder endianType = null;
	private Charset charset = null;

	public CompoundFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] bytes = new byte[fis.available()];
		fis.read(bytes);
		fis.close();

		// header
		byte[] headerBytes = ByteHandler.part(bytes, 0, Header.SIZE);
		header = new Header(headerBytes);
		this.uid = header.getUid();
		this.revision = header.getRevision();
		this.version = header.getVersion();
		this.endianType = header.getEndianType();
		this.charset = header.getCharset();

		// sector structure
		// sectorTable
		byte[] sectorBytes = ByteHandler.part(bytes, Header.SIZE, bytes.length - Header.SIZE);
		sectorTable = new SectorTable(sectorBytes, header.getSizeOfSector());

		// sectorAllocationTable
		msat = new MSAT(sectorTable, header.getMsatBytes(), endianType);
		sat = new SAT(sectorTable, msat, endianType);

		// directory
		directoryEntryTable = new DirectoryEntryTable(sectorTable, sat, header.getFirstDirectoryStreamSectorId(),
				header.getSizeOfSector(), endianType, charset);

		// short stream structure
		// shortStreamTable
		DirectoryEntry rootStorage = directoryEntryTable.get("Root Entry");
		shortStreamTable = new ShortStreamTable(sectorTable, sat, rootStorage.getFirstSectorID(),
				header.getSizeOfShortStream());

		// shortStreamAllocationTable
		ssat = new SSAT(shortStreamTable, header.getFirstSSATId(), sectorTable, sat, endianType);
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
}
