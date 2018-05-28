package compoundFile.component.shortStream;

import java.util.HashMap;
import java.util.Map;

import compoundFile.component.sector.Sector;
import compoundFile.component.sector.SectorTable;
import compoundFile.material.BytesBlock;

public class ShortStreamTable {
	private SSAT ssat = null;

	private Map<Integer, ShortStream> shortStreamMap = new HashMap<Integer, ShortStream>();

	private int size;
	private int sizeOfShortStream;

	public ShortStreamTable(SectorTable sectorTable, int firstShortStreamSectorID, int firstSSATID,
			int sizeOfShortStream) {
		Sector sector = sectorTable.get(firstShortStreamSectorID);
		int shortStreamIDCount = 0;
		while (sector != null) {
			BytesBlock sectorBlock = sector.getBlock();
			for (int i = 0; i < sectorBlock.getLength() / sizeOfShortStream; i++) {
				BytesBlock shortStreamBlock = sectorBlock.subBlock(i * sizeOfShortStream, sizeOfShortStream);
				ShortStream shortStream = new ShortStream(shortStreamIDCount, shortStreamBlock);
				shortStreamMap.put(shortStreamIDCount++, shortStream);
			}
			sector = sectorTable.getNext(sector);
		}
		size = shortStreamMap.size();
		this.sizeOfShortStream = sizeOfShortStream;

		ssat = new SSAT(firstSSATID, sectorTable);
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
	
	public int getSizeOfShortStream() {
		return sizeOfShortStream;
	}

	public int size() {
		return size;
	}
}