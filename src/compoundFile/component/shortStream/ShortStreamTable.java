package compoundFile.component.shortStream;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import compoundFile.component.sat.SSAT;
import compoundFile.component.sector.Sector;
import compoundFile.component.sector.SectorTable;

public class ShortStreamTable {
	private SSAT ssat = null;

	private Map<Integer, ShortStream> shortStreamMap = new HashMap<Integer, ShortStream>();

	private int size;

	public ShortStreamTable(SectorTable sectorTable, int firstShortStreamSectorID, int firstSSATID,
			int sizeOfShortStream, ByteOrder endianType) {
		Sector sector = sectorTable.get(firstShortStreamSectorID);
		int shortStreamIDCount = 0;
		while (sector != null) {
			byte[] sectorBytes = sector.getBytes();
			for (int i = 0; i < sectorBytes.length / sizeOfShortStream; i++) {
				ShortStream shortStream = new ShortStream(shortStreamIDCount, sector, i);
				shortStreamMap.put(shortStreamIDCount++, shortStream);
			}
			sector = sectorTable.getNext(sector);
		}
		size = shortStreamMap.size();

		ssat = new SSAT(firstSSATID, sectorTable, endianType);
	}

	public ShortStream get(int id) {
		return shortStreamMap.get(id);
	}
	
	public ShortStream getNext(ShortStream shortStream) {
		return getNext(shortStream.getID());
	}
	
	public ShortStream getNext(int id) {
		int nextID = ssat.nextID(id);
		return get(nextID);
	}

	public int size() {
		return size;
	}
}