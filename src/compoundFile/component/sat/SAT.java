package compoundFile.component.sat;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import compoundFile.component.sector.Sector;
import compoundFile.util.ByteHandler;

// sector allocation table
public class SAT {
	private List<Integer> sectorIdAllocationTable = new ArrayList<Integer>();

	public SAT(MSAT msat, ByteOrder endianType) {
		List<Sector> satSectors = msat.getSatSectors();
		for (Sector satSector : satSectors) {
			List<Integer> idAllocation = ByteHandler.toIntegerList(satSector.getBytes(), Sector.ID_LENGTH, endianType);
			sectorIdAllocationTable.addAll(idAllocation);
		}
	}

	public int nextSectorID(int id) {
		return sectorIdAllocationTable.get(id);
	}
}
