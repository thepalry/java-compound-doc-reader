package compoundFile.component.sat;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import compoundFile.component.sector.Sector;
import compoundFile.component.sector.SectorTable;
import compoundFile.util.ByteHandler;

// sector allocation table
public class SAT {
	private SectorTable sectorTable;
	private List<Integer> sectorIdAllocationTable = new ArrayList<Integer>();

	public SAT(SectorTable sectorTable, MSAT msat, ByteOrder endianType) {
		this.sectorTable = sectorTable;
		List<Sector> satSectors = msat.getSatSectors();
		for (Sector satSector : satSectors) {
			List<Integer> idAllocation = ByteHandler.toIntegerList(satSector.getBytes(), Sector.ID_LENGTH, endianType);
			sectorIdAllocationTable.addAll(idAllocation);
		}
	}

	public Sector nextSector(Sector sector) {
		return nextSector(sector.getID());
	}

	public Sector nextSector(int id) {
		int nextSectorID = sectorIdAllocationTable.get(id);
		return sectorTable.get(nextSectorID);
	}
}
