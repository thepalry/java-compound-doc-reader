package compoundFile.component.sector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import compoundFile.material.BytesBlock;

public class SectorTable implements Iterable<Sector> {
	private List<Sector> msat = new ArrayList<Sector>();
	private SAT sat = null;

	private List<Sector> sectorList = new LinkedList<Sector>();
	private Map<Integer, Sector> sectorMap = new HashMap<Integer, Sector>();

	private int size;
	private int sizeOfSector;

	public SectorTable(BytesBlock msatBlock, BytesBlock sectorsBlock, int sizeOfSector) {
		for (int i = 0; i < sectorsBlock.getLength() / sizeOfSector; i++) {
			BytesBlock sectorBlock = sectorsBlock.subBlock(sizeOfSector * i, sizeOfSector);
			Sector sector = new Sector(i, sectorBlock);
			sectorMap.put(i, sector);
			sectorList.add(sector);
		}
		size = sectorList.size();
		this.sizeOfSector = sizeOfSector;

		msat = buildMSAT(msatBlock);

		sat = new SAT(msat);
	}

	private List<Sector> buildMSAT(BytesBlock msatBlock) {
		Sector sector = new Sector(-1, msatBlock);
		List<Sector> satSectors = new ArrayList<Sector>();
		while (sector != null) {
			BytesBlock sectorBlock = sector.getBlock();
			List<Integer> satSectorIDs = sectorBlock.readIntList(Sector.ID_LENGTH);
			
			int nextMsatSectorID = satSectorIDs.remove(satSectorIDs.size() - 1);
			for (int satSectorID : satSectorIDs) {
				if (Sector.isSpecialID(satSectorID)) {
					break;
				}
				Sector satSector = this.get(satSectorID);
				satSectors.add(satSector);
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

	public int getSizeOfSector() {
		return sizeOfSector;
	}

	@Override
	public Iterator<Sector> iterator() {
		return sectorList.iterator();
	}
}
