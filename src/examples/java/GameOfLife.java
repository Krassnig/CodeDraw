import codedraw.*;

public class GameOfLife {
	private static final int FIELD_SIZE = 10;

	public static void main(String[] args) {
		final int size = 1 << 6; // must be power of 2
		final int mask = size - 1;

		CodeDraw cd = new CodeDraw(size * FIELD_SIZE, size * FIELD_SIZE);
		EventScanner es = cd.getEventScanner();

		boolean[][] field = createRandomBooleans(size);

		boolean isMouseDown = false;
		boolean setValue = false;

		for (int i = 0; !cd.isClosed(); i++) {
			while (es.hasEventNow()) {
				if (es.hasMouseDownEvent()) {
					MouseDownEvent a = es.nextMouseDownEvent();
					isMouseDown = true;
					// the value that is used to draw cells depends on the state of the cell where the initial click happens
					// if that cell was white then every subsequent move of the mouse will draw black cells.
					int x = a.getX() / FIELD_SIZE;
					int y = a.getY() / FIELD_SIZE;
					setValue = field[x][y] = !field[x][y];
				}
				else if (es.hasMouseUpEvent() || es.hasMouseLeaveEvent()) {
					es.nextEvent();
					isMouseDown = false;
				}
				else if (es.hasMouseMoveEvent()) {
					MouseMoveEvent a = es.nextMouseMoveEvent();
					if (isMouseDown) {
						field[a.getX() / FIELD_SIZE][a.getY() / FIELD_SIZE] = setValue;
					}
				}
				else {
					es.nextEvent();
				}
			}

			// update to next generation only every eighth render
			if (i % 8 == 0) {
				field = simulateNextGeneration(field, mask);
			}

			render(cd, field);
		}
	}

	private static boolean[][] createRandomBooleans(int size) {
		boolean[][] field = new boolean[size][size];

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				field[x][y] = Math.random() > 0.5;
			}
		}

		return field;
	}

	public static boolean[][] simulateNextGeneration(boolean[][] field, int mask) {
		final int radius = 1;
		boolean[][] nextField = new boolean[field.length][field.length];

		for (int x = 0; x < field.length; x++) {
			for (int y = 0; y < field[x].length; y++) {
				int sum = 0;

				for (int xn = -radius; xn <= radius; xn++) {
					for (int yn = -radius; yn <= radius; yn++) {
						// ignore the center    and only sum if the neighbor is alive
						if ((xn != 0 || yn != 0) && field[(x + xn) & mask][(y + yn) & mask]) {
							sum++;
						}
					}
				}

				if (field[x][y]) {
					nextField[x][y] = sum == 2 || sum == 3;
				}
				else {
					nextField[x][y] = sum == 3;
				}
			}
		}

		return nextField;
	}

	public static void render(CodeDraw cd, boolean[][] field) {
		for (int x = 0; x < field.length; x++) {
			for (int y = 0; y < field[x].length; y++) {
				cd.setColor(field[x][y] ? Palette.BLACK : Palette.WHITE);
				cd.fillSquare(FIELD_SIZE * x, FIELD_SIZE * y, FIELD_SIZE);
			}
		}

		cd.show(16);
	}
}
