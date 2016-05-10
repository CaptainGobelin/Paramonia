package Utils;

import java.util.concurrent.atomic.AtomicLong;

public class ID {
	
	private static AtomicLong idCounter = new AtomicLong();

	public static String createID() {
	    return String.valueOf(idCounter.getAndIncrement());
	}

}
