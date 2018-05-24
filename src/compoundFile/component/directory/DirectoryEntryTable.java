package compoundFile.component.directory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import compoundFile.component.sat.SAT;
import compoundFile.component.sector.Sector;
import compoundFile.component.sector.SectorTable;
import compoundFile.util.ByteHandler;

public class DirectoryEntryTable {
	private Map<String, DirectoryEntry> directoryEntryMap = new HashMap<String, DirectoryEntry>();

	public DirectoryEntryTable(SectorTable sectorTable, SAT sat, int firstDirectoryStreamSectorID, int sizeOfSector,
			ByteOrder endianType, Charset charset) {
		Sector directorySector = sectorTable.get(firstDirectoryStreamSectorID);
		while (directorySector != null) {
			for (int i = 0; i < sizeOfSector / DirectoryEntry.SIZE; i++) {
				byte[] directoryEntryRaw = ByteHandler.part(directorySector.getBytes(), i * DirectoryEntry.SIZE,
						DirectoryEntry.SIZE);
				DirectoryEntry de = new DirectoryEntry(directoryEntryRaw, endianType, charset);
				directoryEntryMap.put(de.getName(), de);
			}
			directorySector = sat.nextSector(directorySector);
		}
	}

	public DirectoryEntry get(String name) {
		return directoryEntryMap.get(name);
	}
}
