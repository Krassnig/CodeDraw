import codedraw.*;
import codedraw.events.EventScanner;
import codedraw.events.MouseDownEventArgs;
import codedraw.events.MouseMoveEventArgs;

public class GameOfLife {
	public static void main(String[] args) {
		GameOfLife gof = new GameOfLife(6152, 6, 10);

		for (int i = 0; true; i++) {
			if (i % 8 == 0) {
				// will simulate a new generation every 128ms
				gof.simulate(1);
			}

			// renders about 60 frames per second
			gof.processEvents();
			gof.renderAndWait(16);
		}
	}

	public GameOfLife(int rule, int logSize, int fieldSize) {
		this.rule = rule;
		size = 1 << logSize;
		mask = size - 1;
		this.fieldSize = fieldSize;

		cd = new CodeDraw(size * fieldSize, size * fieldSize);
		esc = new EventScanner(cd);
		field = createRandomBooleans(size);
	}

	private final CodeDraw cd;
	private final EventScanner esc;
	private final int rule;
	private final int size;
	private final int mask;
	private final int fieldSize;

	private boolean[][] field;
	private boolean isMouseDown = false;
	private boolean setValue = false;

	public void processEvents() {
		while (esc.hasNextEventNow()) {
			if (esc.hasMouseDownEvent()) {
				MouseDownEventArgs a = esc.nextMouseDownEvent();
				isMouseDown = true;
				setValue = !field[a.getX() / fieldSize][a.getY() / fieldSize];
			}
			else if (esc.hasMouseUpEvent() || esc.hasMouseLeaveEvent()) {
				esc.nextEvent();
				isMouseDown = false;
			}
			else if (esc.hasMouseMoveEvent()) {
				MouseMoveEventArgs a = esc.nextMouseMoveEvent();
				if (isMouseDown) {
					field[a.getX() / fieldSize][a.getY() / fieldSize] = setValue;
				}
			}
			else {
				esc.nextEvent();
			}
		}
	}

	public void renderAndWait(int milliSeconds) {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				cd.setColor(field[x][y] ? Palette.BLACK : Palette.WHITE);
				cd.fillSquare(fieldSize * x, fieldSize * y, fieldSize);
			}
		}

		cd.show(milliSeconds);
	}

	public void simulate(int generations) {
		for (int g = 0; g < generations; g++) {
			boolean[][] nextField = new boolean[size][size];

			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					nextField[x][y] = filterLifeLike(rule, field[x][y], new boolean[]{
							field[(x + 1) & mask][(y + 1) & mask],
							field[(x + 1) & mask][(y) & mask],
							field[(x + 1) & mask][(y - 1) & mask],
							field[(x) & mask][(y + 1) & mask],

							field[(x) & mask][(y - 1) & mask],
							field[(x - 1) & mask][(y + 1) & mask],
							field[(x - 1) & mask][(y) & mask],
							field[(x - 1) & mask][(y - 1) & mask],
					});
				}
			}

			field = nextField;
		}
	}

	private static boolean filterLifeLike(int rule, boolean center, boolean[] n) {
		int sum = 0;
		for (int i = 0; i < n.length; i++) {
			sum += n[i] ? 1 : 0;
		}
		return ((rule >> (sum + (9 * (center ? 1 : 0)))) & 1) == 1;
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
}
