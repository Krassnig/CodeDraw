import codedraw.*;

public class SinCos {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();
		int radius = 100;

		for (double i = 0; true; i += Math.PI / 64) {
			cd.clear();

			cd.setColor(Palette.BLACK);
			cd.drawCircle(300, 300, radius);

			cd.setColor(Palette.BLUE);
			double newx = 300 + radius * Math.cos(i);
			double newy = 300 + radius * Math.sin(i);
			cd.drawLine(300, 300, newx, 300);
			cd.drawLine(newx, 300, newx, newy);

			cd.setColor(Palette.RED);
			cd.drawLine(300, 300, newx, newy);

			cd.show(16);
		}
	}
}
