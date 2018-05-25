package compoundFile.component.sat;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import compoundFile.component.sector.Sector;
import compoundFile.component.sector.SectorTable;
import compoundFile.component.shortStream.ShortStream;
import compoundFile.component.shortStream.ShortStreamTable;
import compoundFile.util.ByteHandler;

// short stream allocation table
public class SSAT {
	ShortStreamTable shortStreamTable = null;
	private List<Integer> shortStreamIdAllocationTable = new ArrayList<Integer>();

	public SSAT(ShortStreamTable shortStreamTable, int firstSSATID, SectorTable sectorTable, SAT sat,
			ByteOrder endianType) {
		this.shortStreamTable = shortStreamTable;
		Sector ssatSector = sectorTable.get(firstSSATID);
		while (ssatSector != null) {
			List<Integer> newSsat = ByteHandler.toIntegerList(ssatSector.getBytes(), Sector.ID_LENGTH, endianType);
			shortStreamIdAllocationTable.addAll(newSsat);
			int nextSectorID = sat.nextSectorID(ssatSector.getID());
			ssatSector = sectorTable.get(nextSectorID);
		}
	}
	
	public ShortStream nextShortStream(ShortStream shortStream) {
		return nextShortStream(shortStream.getID());
	}

	public ShortStream nextShortStream(int id) {
		int nextSectorID = shortStreamIdAllocationTable.get(id);
		return shortStreamTable.get(nextSectorID);
	}

}
