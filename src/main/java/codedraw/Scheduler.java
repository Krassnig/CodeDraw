package codedraw;

class Scheduler {
	public Scheduler(long timeIntervalBetweenTasksInMilliseconds, boolean dropTasks) {
		this.timeIntervalMilliseconds = timeIntervalBetweenTasksInMilliseconds;
		this.dropTasks = dropTasks;
	}

	private final long timeIntervalMilliseconds;
	private final boolean dropTasks;
	private final long startTime = now();

	private long doneTasks = 0;
	private long droppedTasks = 0;

	public boolean shouldDoTask() {
		long targetTaskTotalNow = targetTaskTotalNow();

		if (targetTaskTotalNow < totalCompletedTasks()) {
			return false;
		}
		else if (targetTaskTotalNow == totalCompletedTasks()) {
			doneTasks++;
			return true;
		}
		else if (dropTasks) {
			droppedTasks += targetTaskTotalNow - totalCompletedTasks();
			return false;
		}
		else {
			doneTasks++;
			return true;
		}
	}

	public long timeUntilNextTask() {
		return startTime + timeIntervalMilliseconds * totalCompletedTasks() - now();
	}

	private long totalCompletedTasks() {
		return doneTasks + droppedTasks;
	}

	private long targetTaskTotalNow() {
		return (now() - startTime) / timeIntervalMilliseconds;
	}

	private static long now() {
		return System.currentTimeMillis();
	}

}
