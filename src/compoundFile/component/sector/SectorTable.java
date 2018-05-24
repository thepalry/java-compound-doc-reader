package compoundFile.component.sector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import compoundFile.util.ByteHandler;

public class SectorTable implements Iterable<Sector> {
	int size;
	private List<Sector> sectorList = new LinkedList<Sector>();
	private Map<Integer, Sector> sectorMap = new HashMap<Integer, Sector>();

	public SectorTable(byte[] sectorBytes, int sizeOfSector) {
		for (int i = 0; i < sectorBytes.length / sizeOfSector; i++) {
			byte[] sectorPart = ByteHandler.part(sectorBytes, sizeOfSector * i, sizeOfSector);
			Sector sector = new Sector(i, sectorPart);
			sectorMap.put(i, sector);
			sectorList.add(sector);
		}
		size = sectorList.size();
	}

	public Sector get(int id) {
		return sectorMap.get(id);
	}

	public int size() {
		return size;
	}

	@Override
	public Iterator<Sector> iterator() {
		return sectorList.iterator();
	}
}
