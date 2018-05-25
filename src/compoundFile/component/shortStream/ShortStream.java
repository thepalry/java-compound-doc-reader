package compoundFile.component.shortStream;

import compoundFile.material.BytesBlock;

public class ShortStream {
	private int id;
	private BytesBlock block;

	public ShortStream(int id, BytesBlock block) {
		this.id = id;
		this.block = block;
	}

	public int getID() {
		return id;
	}

	public BytesBlock getBlock() {
		return block;
	}

}
