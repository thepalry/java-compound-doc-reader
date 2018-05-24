package compoundFile.component.shortStream;

import compoundFile.component.sector.Sector;
import compoundFile.util.ByteHandler;

public class ShortStream {
	private static int SIZE = 64;

	private int id;
	private Sector storedSector;
	private int storedIndex;

	public ShortStream(int id, Sector storedSector, int index) {
		this.id = id;
		this.storedSector = storedSector;
		this.storedIndex = index;
	}

	public void setBytes(byte[] bytes) {
		byte[] sectorBytes = storedSector.getBytes();
		System.arraycopy(bytes, 0, sectorBytes, storedIndex * SIZE, SIZE);
	}

	public byte[] getBytes() {
		return ByteHandler.part(storedSector.getBytes(), storedIndex * SIZE, SIZE);
	}

	public int getID() {
		return id;
	}

}
