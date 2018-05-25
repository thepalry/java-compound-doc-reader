package compoundFile.component.shortStream;

import java.util.ArrayList;
import java.util.List;

import compoundFile.component.sector.Sector;
import compoundFile.component.sector.SectorTable;
import compoundFile.material.BytesBlock;

// short stream allocation table
public class SSAT {
	private List<Integer> shortStreamIdAllocationTable = new ArrayList<Integer>();

	public SSAT(int firstSSATID, SectorTable sectorTable) {
		Sector ssatSector = sectorTable.get(firstSSATID);
		while (ssatSector != null) {
			BytesBlock ssatSectorBlock = ssatSector.getBlock();
			List<Integer> newSsat = ssatSectorBlock.readIntList(Sector.ID_LENGTH);
			shortStreamIdAllocationTable.addAll(newSsat);
			ssatSector = sectorTable.getNext(ssatSector);
		}
	}

	public int nextID(int id) {
		return shortStreamIdAllocationTable.get(id);
	}

}
