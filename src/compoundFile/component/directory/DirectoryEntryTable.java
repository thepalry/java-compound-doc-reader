package compoundFile.component.directory;

import java.util.HashMap;
import java.util.Map;

import compoundFile.component.sector.Sector;
import compoundFile.component.sector.SectorTable;
import compoundFile.material.BytesBlock;

public class DirectoryEntryTable {
	public static final String ROOT_ENTRY = "ROOT ENTRY";

	private Map<String, DirectoryEntry> directoryEntryMap = new HashMap<String, DirectoryEntry>();

	public DirectoryEntryTable(SectorTable sectorTable, int firstDirectoryStreamSectorID) {
		Sector directorySector = sectorTable.get(firstDirectoryStreamSectorID);
		while (directorySector != null) {
			for (int i = 0; i < sectorTable.getSizeOfSector() / DirectoryEntry.SIZE; i++) {
				BytesBlock directorySectorBlock = directorySector.getBlock();
				BytesBlock entryBlock = directorySectorBlock.subBlock(i * DirectoryEntry.SIZE, DirectoryEntry.SIZE);
				DirectoryEntry de = new DirectoryEntry(entryBlock);
				directoryEntryMap.put(de.getName(), de);
			}
			directorySector = sectorTable.getNext(directorySector);
		}
	}

	public DirectoryEntry get(String name) {
		return directoryEntryMap.get(name);
	}
}
