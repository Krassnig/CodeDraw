import codedraw.*;

public class GameOfLife {

	public static void main(String[] args) {
		GameOfLife gof = new GameOfLife(6, 10);

		while (true) {
			gof.simulateAndWait(100);
		}
	}

	public GameOfLife(int logSize, int fieldSize) {
		this.logSize = logSize;
		size = 1 << logSize;
		mask = size - 1;
		this.fieldSize = fieldSize;

		cd = new CodeDraw(size * fieldSize, size * fieldSize);
		field = createRandomBooleans(size);

		bindEvents();
	}

	private CodeDraw cd;
	private boolean[][] field;
	private int logSize;
	private int size;
	private int mask;
	private int fieldSize;
	private boolean isMouseDown = false;
	private boolean setValue = false;

	private void bindEvents() {
		cd.onMouseDown((c, a) -> {
			isMouseDown = true;
			setValue = !field[a.getX() / fieldSize][a.getY() / fieldSize];
		});
		cd.onMouseUp((c, a) -> isMouseDown = false);
		cd.onMouseLeave((c, a) -> isMouseDown = false);
		cd.onMouseMove((c, a) -> {
			if (isMouseDown) {
				int x = a.getX() / fieldSize;
				int y = a.getY() / fieldSize;
				field[x][y] = setValue;
				sendToBuffer();
				cd.show();
			}
		});
	}

	public void simulateAndWait(int waitMilliseconds) {
		simulate(1);
		sendToBuffer();
		cd.show(waitMilliseconds);
	}

	private void simulate(int generations) {
		for (int g = 0; g < generations; g++) {
			boolean[][] nextField = new boolean[size][size];

			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					nextField[x][y] = filterGameOfLife(6152, field[x][y], new boolean[]{
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

	private void sendToBuffer() {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				cd.setColor(field[x][y] ? Palette.BLACK : Palette.WHITE);
				cd.fillSquare(fieldSize * x, fieldSize * y, fieldSize);
			}
		}
	}

	private static boolean filterGameOfLife(int rule, boolean center, boolean[] n) {
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
