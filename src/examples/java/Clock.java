import codedraw.*;
import codedraw.drawing.Corner;

import java.time.OffsetDateTime;

public class Clock {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();

		cd.setCorner(Corner.ROUND);
		double tau = Math.PI * 2;

		while (!cd.isClosed()) {
			OffsetDateTime time = OffsetDateTime.now();

			cd.clear();
			cd.setColor(Palette.BLACK);

			// hour dots
			for (double j = 0; j < tau; j += tau / 12) {
				cd.fillCircle(Math.cos(j) * 100 + 300, Math.sin(j) * 100 + 300, 4);
			}

			// hour hand
			double hourAngle = ((time.getHour() % 12) / 12D) * tau - tau / 4;
			cd.setLineWidth(4);
			cd.drawLine(300, 300, Math.cos(hourAngle) * 50 + 300, Math.sin(hourAngle) * 50 + 300);

			double minuteAngle = (time.getMinute() / 60D) * tau - tau / 4;
			cd.setLineWidth(2);
			cd.drawLine(300, 300, Math.cos(minuteAngle) * 80 + 300, Math.sin(minuteAngle) * 80 + 300);

			double secondAngle = (time.getSecond() / 60D) * tau - tau / 4;
			cd.setLineWidth(1);
			cd.drawLine(300, 300, Math.cos(secondAngle) * 100 + 300, Math.sin(secondAngle) * 100 + 300);

			cd.show(16);
		}
	}
}
