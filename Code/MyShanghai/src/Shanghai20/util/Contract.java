package Shanghai20.util;

public final class Contract {

	public Contract() {
	}
	
	public static void checkCondition(boolean condition, String... msg) {
		if (!condition) {
			throw new AssertionError(msg);
		}
	}

}
