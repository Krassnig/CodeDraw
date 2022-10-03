import codedraw.*;

public class SinCos {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();
		int radius = 100;

		for (double i = 0; !cd.isClosed(); i += Math.PI / 64) {
			cd.clear();

			cd.setColor(Palette.BLACK);
			cd.drawCircle(300, 300, radius);

			cd.setColor(Palette.BLUE);
			double newX = 300 + radius * Math.cos(i);
			double newY = 300 + radius * Math.sin(i);
			cd.drawLine(300, 300, newX, 300);
			cd.drawLine(newX, 300, newX, newY);

			cd.setColor(Palette.RED);
			cd.drawLine(300, 300, newX, newY);

			cd.show(16);
		}
	}
}
