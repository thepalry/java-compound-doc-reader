package compoundFile.component.shortStream;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import compoundFile.component.sector.Sector;
import compoundFile.component.sector.SectorTable;
import compoundFile.util.ByteHandler;

// short stream allocation table
public class SSAT {
	private List<Integer> shortStreamIdAllocationTable = new ArrayList<Integer>();

	public SSAT(int firstSSATID, SectorTable sectorTable, ByteOrder endianType) {
		Sector ssatSector = sectorTable.get(firstSSATID);
		while (ssatSector != null) {
			List<Integer> newSsat = ByteHandler.toIntegerList(ssatSector.getBytes(), Sector.ID_LENGTH, endianType);
			shortStreamIdAllocationTable.addAll(newSsat);
			ssatSector = sectorTable.getNext(ssatSector);
		}
	}

	public int nextID(int id) {
		return shortStreamIdAllocationTable.get(id);
	}

}
