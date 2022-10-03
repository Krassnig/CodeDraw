import codedraw.*;

public class Mandala {
	public static void main(String[] args) {
		int size = 800;
		CodeDraw cd = new CodeDraw(size, size);

		double r = (size / 2D) * 0.95;
		double c = size / 2D;

		cd.clear(Palette.BLACK);
		cd.setColor(Palette.WHITE);

		cd.drawCircle(c, c, r);

		for (double i = 0, j = Math.PI / 2; !cd.isClosed(); i += 0.05538459 * 2, j += 0.02598203 * 2) {

			double x1 = c + Math.cos(i) * r;
			double y1 = c + Math.sin(i) * r;

			double x2 = c + Math.cos(j) * r;
			double y2 = c + Math.sin(j) * r;

			cd.drawLine(x1, y1, x2, y2);

			cd.show(20);
		}
	}
}
