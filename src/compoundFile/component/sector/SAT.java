package compoundFile.component.sector;

import java.util.ArrayList;
import java.util.List;

import compoundFile.material.BytesBlock;

// sector allocation table
public class SAT {
	private List<Integer> sectorIdAllocationTable = new ArrayList<Integer>();

	public SAT(List<Sector> satSectors) {
		for (Sector satSector : satSectors) {
			BytesBlock satSectorBlock = satSector.getBlock();
			List<Integer> idAllocation = satSectorBlock.readIntList(Sector.ID_LENGTH);
			sectorIdAllocationTable.addAll(idAllocation);
		}
	}

	public int nextID(int id) {
		return sectorIdAllocationTable.get(id);
	}
}
