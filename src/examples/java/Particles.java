import codedraw.*;
import codedraw.events.*;

public class Particles {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();
		EventScanner es = new EventScanner(cd);
		cd.setColor(Palette.RED);

		final int particleCount = 100000;
		final double exponent = -0.5 - 0.5 / Math.sqrt(2);

		double speed = 100;
		int mouseX = 300;
		int mouseY = 300;
		double[] particlesX = new double[particleCount];
		double[] particlesY = new double[particleCount];

		// initialize the particle at random positions on the canvas
		for (int i = 0; i < particleCount; i++) {
			particlesX[i] = Math.random() * cd.getWidth();
			particlesY[i] = Math.random() * cd.getHeight();
		}

		// start an endless loop until tha CodeDraw window is closed
		while (!es.isClosed()) {

			// works through all events currently available
			while (es.hasEventNow()) {
				if (es.hasMouseMoveEvent()) {
					MouseMoveEventArgs a = es.nextMouseMoveEvent();
					mouseX = a.getX();
					mouseY = a.getY();
				}
				else if (es.hasMouseClickEvent()) {
					MouseClickEventArgs a = es.nextMouseClickEvent();
					if (a.getMouseButton() == MouseButton.LEFT) {
						speed += 50;
					}
					else if (a.getMouseButton() == MouseButton.RIGHT) {
						speed -= 50;
					}
				}
				else {
					// discards events that are not used
					es.nextEvent();
				}
			}

			// calculates the new position of all particles
			for (int i = 0; i < particleCount; i++) {
				double distanceX = mouseX - particlesX[i];
				double distanceY = mouseY - particlesY[i];

				double movementFactor = speed * Math.pow(distanceX * distanceX + distanceY * distanceY, exponent);

				particlesX[i] += distanceX * movementFactor;
				particlesY[i] += distanceY * movementFactor;
			}

			// draws the particles
			cd.clear(Palette.BLACK);
			for (int i = 0; i < particleCount; i++) {
				cd.drawPixel(particlesX[i], particlesY[i]);
			}

			cd.show(8);
		}
	}
}