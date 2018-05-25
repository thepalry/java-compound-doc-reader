package compoundFile.component.shortStream;

import java.util.HashMap;
import java.util.Map;

import compoundFile.component.sat.SAT;
import compoundFile.component.sector.Sector;
import compoundFile.component.sector.SectorTable;

public class ShortStreamTable {
	int size;
	private Map<Integer, ShortStream> shortStreamMap = new HashMap<Integer, ShortStream>();

	public ShortStreamTable(SectorTable sectorTable, SAT sat, int firstShortStreamSectorID, int sizeOfShortStream) {
		Sector sector = sectorTable.get(firstShortStreamSectorID);
		int shortStreamIDCount = 0;
		while (sector != null) {
			byte[] sectorBytes = sector.getBytes();
			for (int i = 0; i < sectorBytes.length / sizeOfShortStream; i++) {
				ShortStream shortStream = new ShortStream(shortStreamIDCount, sector, i);
				shortStreamMap.put(shortStreamIDCount++, shortStream);
			}
			int nextSectorID = sat.nextSectorID(sector.getID());
			sector = sectorTable.get(nextSectorID);
		}
		size = shortStreamMap.size();
	}

	public ShortStream get(int id) {
		return shortStreamMap.get(id);
	}

	public int size() {
		return size;
	}
}