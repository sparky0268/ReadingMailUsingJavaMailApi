package constants;

public final class StoreFields {
	public static final String storeProtocol = "imap";
	public static final String host = "outlook.office365.com";
	public static final String user = "<your email>";
	public static final String password = "<your pwd>";
	public static final int port = 993;
	public static boolean debug = false;
	public static boolean readEnable = true;

	private StoreFields() {
		// best practice
	}

}
