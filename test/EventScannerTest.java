import codedraw.CodeDraw;
import codedraw.events.EventScanner;

import java.awt.*;

public class EventScannerTest {

	private static final CodeDraw cd = new CodeDraw();
	private static final EventScanner scanner = new EventScanner(cd);
	public static void main(String[] args) {
		// testScannerHasNextEvent();
		//testScannerNextEvent();
		testScannerWrongNextEvent();
	}

	private static void testScannerWrongNextEvent() {
		while(true) {
			System.out.println("Test");
			System.out.println(scanner.waitForKeyUpEvent());
		}
	}

	private static void testScannerHasNextEvent(){

		cd.clear(Color.RED);
		cd.show();
		System.out.println(scanner.hasEvent());
	}

}
