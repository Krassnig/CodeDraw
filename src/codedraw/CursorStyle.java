package codedraw;

import java.awt.*;

/**
 * CursorStyle is used to specify how the cursor looks when hovering over the canvas.
 */
public class CursorStyle {


	public static CursorStyle DEFAULT = new CursorStyle(Cursor.getDefaultCursor());
	public static CursorStyle CROSS_HAIR = new CursorStyle(Cursor.CROSSHAIR_CURSOR);
	public static CursorStyle TEXT = new CursorStyle(Cursor.TEXT_CURSOR);
	public static CursorStyle WAIT = new CursorStyle(Cursor.WAIT_CURSOR);
	public static CursorStyle SOUTH_WEST_RESIZE = new CursorStyle(Cursor.SW_RESIZE_CURSOR);
	public static CursorStyle SOUTH_EAST_RESIZE = new CursorStyle(Cursor.SE_RESIZE_CURSOR);
	public static CursorStyle NORTH_WEST_RESIZE = new CursorStyle(Cursor.NW_RESIZE_CURSOR);
	public static CursorStyle NORTH_EAST_RESIZE = new CursorStyle(Cursor.NE_RESIZE_CURSOR);
	public static CursorStyle NORTH_RESIZE = new CursorStyle(Cursor.N_RESIZE_CURSOR);
	public static CursorStyle SOUTH_RESIZE = new CursorStyle(Cursor.S_RESIZE_CURSOR);
	public static CursorStyle WEST_RESIZE = new CursorStyle(Cursor.W_RESIZE_CURSOR);
	public static CursorStyle EAST_RESIZE = new CursorStyle(Cursor.E_RESIZE_CURSOR);
	public static CursorStyle HAND = new CursorStyle(Cursor.HAND_CURSOR);
	public static CursorStyle MOVE = new CursorStyle(Cursor.MOVE_CURSOR);

	private final Cursor cursor;

	/**
	 * Creates a new CursorStyle from an image. Can be set through {@link CodeDraw#setCursorStyle(CursorStyle)}
	 * @param image appearance of the cursor when hovering over the canvas
	 */
	public CursorStyle(Image image) {
		this.cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "CodeDraw Custom Cursor " + image.hashCode());
	}

	private CursorStyle(int cursorType) {
		this(Cursor.getPredefinedCursor(cursorType));
	}

	private CursorStyle(Cursor cursor) {
		this.cursor = cursor;
	}

	Cursor getCursor() {
		return cursor;
	}
}
