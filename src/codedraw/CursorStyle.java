package codedraw;

import java.awt.*;
import java.util.Objects;

/**
 * CursorStyle is used to specify how the cursor looks like when hovering over the CodeDraw canvas.
 */
public class CursorStyle {
	private static Toolkit defaultToolkit = Toolkit.getDefaultToolkit();

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

	/**
	 * Creates a new CursorStyle from an image.
	 * @param image Appearance of the cursor when hovering over the canvas.
	 */
	public CursorStyle(Image image) {
		this(image, 0, 0);
	}

	/**
	 * Creates a new CursorStyle from an image.
	 * @param image Appearance of the cursor when hovering over the canvas.
	 * @param x The click position relative to the image.
	 * @param y The click position relative to the image.
	 */
	public CursorStyle(Image image, int x, int y) {
		this.cursor = defaultToolkit.createCustomCursor(image, new Point(x, y), "CodeDraw Custom Cursor " + Objects.hash(image, x, y));
	}

	private CursorStyle(int cursorType) {
		this(Cursor.getPredefinedCursor(cursorType));
	}

	private CursorStyle(Cursor cursor) {
		this.cursor = cursor;
	}

	private final Cursor cursor;

	Cursor getCursor() {
		return cursor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CursorStyle that = (CursorStyle) o;
		return Objects.equals(cursor, that.cursor);
	}

	@Override
	public int hashCode() {
		return cursor.hashCode();
	}

	@Override
	public String toString() {
		return "Cursor: " + cursor.getName();
	}
}
