package compoundFile.component.sat;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import compoundFile.component.sector.Sector;
import compoundFile.component.sector.SectorTable;
import compoundFile.util.ByteHandler;

// master sector allocation table
public class MSAT {
	private List<Sector> satSectors = new ArrayList<Sector>();

	public MSAT(SectorTable sectorTable, byte[] bytes, ByteOrder endianType) {
		int nextMsatSectorID = setSatSectors(new Sector(-1, bytes), sectorTable, endianType);
		while (Sector.isSpecialID(nextMsatSectorID) == false) {
			Sector msatSector = sectorTable.get(nextMsatSectorID);
			nextMsatSectorID = setSatSectors(msatSector, sectorTable, endianType);
		}
	}

	private int setSatSectors(Sector sector, SectorTable sectorTable, ByteOrder endianType) {
		List<Integer> satSectorIDs = ByteHandler.toIntegerList(sector.getBytes(), Sector.ID_LENGTH, endianType);
		int nextMsatSectorID = satSectorIDs.remove(satSectorIDs.size() - 1);
		for (int satSectorID : satSectorIDs) {
			if (Sector.isSpecialID(satSectorID)) {
				break;
			}
			satSectors.add(sectorTable.get(satSectorID));
		}
		return nextMsatSectorID;
	}

	public List<Sector> getSatSectors() {
		return satSectors;
	}
}
