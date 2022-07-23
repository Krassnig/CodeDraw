package codedraw;

class FPSCounter {
	public FPSCounter() {

	}

	private int fps = 0;
	private long timeSinceLastFPSUpdate = currentTimeMilliseconds();
	private int currentFrameCount = 0;

	public void countFrame() {
		long now = currentTimeMilliseconds();
		if (now - timeSinceLastFPSUpdate >= 1000) {
			fps = currentFrameCount;
			currentFrameCount = 0;
			timeSinceLastFPSUpdate = timeSinceLastFPSUpdate + 1000;
		}

		currentFrameCount++;
	}

	public int getFPS() {
		return fps;
	}

	private static long currentTimeMilliseconds() {
		return System.nanoTime() / 1000000;
	}
}
