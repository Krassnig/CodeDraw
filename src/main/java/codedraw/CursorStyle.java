package codedraw;

import codedraw.drawing.Canvas;

import java.awt.*;
import java.util.Objects;

/**
 * CursorStyle is used to specify how the cursor looks like when hovering over the CodeDraw canvas.
 */
public class CursorStyle {
	public static final CursorStyle DEFAULT = new CursorStyle(Cursor.getDefaultCursor());
	public static final CursorStyle CROSS_HAIR = new CursorStyle(Cursor.CROSSHAIR_CURSOR);
	public static final CursorStyle TEXT = new CursorStyle(Cursor.TEXT_CURSOR);
	public static final CursorStyle WAIT = new CursorStyle(Cursor.WAIT_CURSOR);
	public static final CursorStyle SOUTH_WEST_RESIZE = new CursorStyle(Cursor.SW_RESIZE_CURSOR);
	public static final CursorStyle SOUTH_EAST_RESIZE = new CursorStyle(Cursor.SE_RESIZE_CURSOR);
	public static final CursorStyle NORTH_WEST_RESIZE = new CursorStyle(Cursor.NW_RESIZE_CURSOR);
	public static final CursorStyle NORTH_EAST_RESIZE = new CursorStyle(Cursor.NE_RESIZE_CURSOR);
	public static final CursorStyle NORTH_RESIZE = new CursorStyle(Cursor.N_RESIZE_CURSOR);
	public static final CursorStyle SOUTH_RESIZE = new CursorStyle(Cursor.S_RESIZE_CURSOR);
	public static final CursorStyle WEST_RESIZE = new CursorStyle(Cursor.W_RESIZE_CURSOR);
	public static final CursorStyle EAST_RESIZE = new CursorStyle(Cursor.E_RESIZE_CURSOR);
	public static final CursorStyle HAND = new CursorStyle(Cursor.HAND_CURSOR);
	public static final CursorStyle MOVE = new CursorStyle(Cursor.MOVE_CURSOR);

	/**
	 * Creates a new CursorStyle from an image.
	 * The top left corner of the image will be the click position.
	 * @param image any image
	 */
	public CursorStyle(Canvas image) {
		this(checkParameterNull(image, "image"), 0, 0);
	}

	/**
	 * Creates a new CursorStyle from an image.
	 * @param image any image
	 * @param x The click position relative to the top of the image.
	 * @param y The click position relative to the left of the image.
	 */
	public CursorStyle(Canvas image, int x, int y) {
		this.cursor = Toolkit.getDefaultToolkit().createCustomCursor(
				checkParameterNull(image, "image").toBufferedImage(),
				new Point(x, y),
				"CodeDraw Custom Cursor " + Objects.hash(image, x, y)
		);
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

	private static <T> T checkParameterNull(T parameter, String parameterName) {
		if (parameter == null)
			throw new IllegalArgumentException("The parameter " + parameterName + " cannot be null.");
		else
			return parameter;
	}
}
