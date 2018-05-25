package compoundFile.component.sector;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import compoundFile.util.ByteHandler;

public class SectorTable implements Iterable<Sector> {
	private SAT sat = null;

	private List<Sector> sectorList = new LinkedList<Sector>();
	private Map<Integer, Sector> sectorMap = new HashMap<Integer, Sector>();

	private List<Sector> msat = new ArrayList<Sector>();

	private int size;

	public SectorTable(byte[] msatBytes, byte[] sectorBytes, int sizeOfSector, ByteOrder endianType) {
		for (int i = 0; i < sectorBytes.length / sizeOfSector; i++) {
			byte[] sectorPart = ByteHandler.part(sectorBytes, sizeOfSector * i, sizeOfSector);
			Sector sector = new Sector(i, sectorPart);
			sectorMap.put(i, sector);
			sectorList.add(sector);
		}
		size = sectorList.size();

		msat = buildMSAT(msatBytes, endianType);

		sat = new SAT(msat, endianType);
	}

	private List<Sector> buildMSAT(byte[] msatBytes, ByteOrder endianType) {
		Sector sector = new Sector(-1, msatBytes);
		List<Sector> satSectors = new ArrayList<Sector>();
		while (sector != null) {
			List<Integer> satSectorIDs = ByteHandler.toIntegerList(sector.getBytes(), Sector.ID_LENGTH, endianType);
			int nextMsatSectorID = satSectorIDs.remove(satSectorIDs.size() - 1);
			for (int satSectorID : satSectorIDs) {
				if (Sector.isSpecialID(satSectorID)) {
					break;
				}
				satSectors.add(get(satSectorID));
			}
			sector = this.get(nextMsatSectorID);
		}
		return satSectors;
	}

	public Sector get(int id) {
		return sectorMap.get(id);
	}

	public Sector getNext(int id) {
		return sectorMap.get(sat.nextID(id));
	}

	public Sector getNext(Sector sector) {
		return getNext(sector.getID());
	}

	public int size() {
		return size;
	}

	@Override
	public Iterator<Sector> iterator() {
		return sectorList.iterator();
	}
}
