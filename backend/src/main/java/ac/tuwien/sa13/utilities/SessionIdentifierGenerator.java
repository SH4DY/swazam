package ac.tuwien.sa13.utilities;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class SessionIdentifierGenerator {
	  private static SecureRandom random = new SecureRandom();

	  public static String nextSessionId() {
	    return new BigInteger(130, random).toString(32);
	  }
	}
