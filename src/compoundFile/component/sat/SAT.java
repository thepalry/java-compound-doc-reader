package compoundFile.component.sat;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import compoundFile.component.sector.Sector;
import compoundFile.util.ByteHandler;

// sector allocation table
public class SAT {
	private List<Integer> sectorIdAllocationTable = new ArrayList<Integer>();

	public SAT(List<Sector> satSectors, ByteOrder endianType) {
		for (Sector satSector : satSectors) {
			List<Integer> idAllocation = ByteHandler.toIntegerList(satSector.getBytes(), Sector.ID_LENGTH, endianType);
			sectorIdAllocationTable.addAll(idAllocation);
		}
	}

	public int nextID(int id) {
		return sectorIdAllocationTable.get(id);
	}
}
