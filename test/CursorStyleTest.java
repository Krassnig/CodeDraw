import codedraw.CodeDraw;
import codedraw.CursorStyle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import static codedraw.CursorStyle.*;

public class CursorStyleTest {
	private static final CursorStyle[] CURSORS = {
			DEFAULT,
			CROSS_HAIR,
			TEXT,
			WAIT,
			SOUTH_WEST_RESIZE,
			SOUTH_EAST_RESIZE,
			NORTH_WEST_RESIZE,
			NORTH_EAST_RESIZE,
			NORTH_RESIZE,
			SOUTH_RESIZE,
			WEST_RESIZE,
			EAST_RESIZE,
			HAND,
			MOVE
	};
	private static Image cursorImage;

	static {
		try {
			cursorImage = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(
					"iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAABGdBTUEAALGPC/xh" +
							"BQAAAA9QTFRF/59o/0RHY2Np/8iZAAAAQFvSagAAAAlwSFlzAAAOwgAADsIBFShK" +
							"gAAAADtJREFUGNNtyMENACAMQlF0AruBMoKM4P47eaAeTPovkIe/9k4Mb4/w4TJ1" +
							"0kJyJrAGJUDbAMmAI1QBFz7NBPgXRK/qAAAAAElFTkSuQmCC"
			)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//testAllPredefinedCursors();
		//testCustomCursor();
	}


	private static void testAllPredefinedCursors() {
		CodeDraw codeDraw = new CodeDraw();
		for (CursorStyle cursorStyle: CURSORS) {
			codeDraw.setCursorStyle(cursorStyle);
			codeDraw.show(2000);
		}
		codeDraw.dispose();
	}


	private static void testCustomCursor() {
		CodeDraw codeDraw = new CodeDraw();
		CursorStyle cursorStyle = new CursorStyle(cursorImage);
		codeDraw.setCursorStyle(cursorStyle);
	}


}
