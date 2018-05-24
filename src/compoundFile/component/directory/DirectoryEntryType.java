package compoundFile.component.directory;

public enum DirectoryEntryType {
	EMPTY, USER_STORAGE, USER_STREAM, LOCK_BYTES, PROPERTY, ROOT_STORAGE;
	
	public static DirectoryEntryType valueOf(int n) {
		return DirectoryEntryType.values()[n];
	}
}
