package compoundFile.component.sector;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import compoundFile.component.sat.MSAT;
import compoundFile.component.sat.SAT;
import compoundFile.util.ByteHandler;

public class SectorTable implements Iterable<Sector> {
	private MSAT msat = null;
	private SAT sat = null;
	
	private List<Sector> sectorList = new LinkedList<Sector>();
	private Map<Integer, Sector> sectorMap = new HashMap<Integer, Sector>();
	
	private int size;

	public SectorTable(byte[] sectorBytes, int sizeOfSector, ByteOrder endianType, byte[] msatBytes) {
		for (int i = 0; i < sectorBytes.length / sizeOfSector; i++) {
			byte[] sectorPart = ByteHandler.part(sectorBytes, sizeOfSector * i, sizeOfSector);
			Sector sector = new Sector(i, sectorPart);
			sectorMap.put(i, sector);
			sectorList.add(sector);
		}
		size = sectorList.size();
		
		msat = new MSAT(this, msatBytes, endianType);
		sat = new SAT(msat, endianType);
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
