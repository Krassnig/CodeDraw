package codedraw;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

class EventLoop {
	public EventLoop() {
		thread = new Thread(this::eventLoop);
		thread.setDaemon(true);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	private Thread thread;
	private ConcurrentQueue<Runnable> queue = new ConcurrentQueue<>();

	public void queue(Runnable runnable) {
		queue.push(runnable);
	}

	public boolean isCurrentThreadOnEventLoop() {
		return thread.equals(Thread.currentThread());
	}

	private void eventLoop() {
		while (true) {
			try {
				queue.pop().run();
			}
			catch (Throwable t) {
				t.printStackTrace();
				JOptionPane.showMessageDialog(null, stackTraceToString(t),"An exception was thrown inside of an event!", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
	}

	private static String stackTraceToString(Throwable t) {
		try (
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw)
		) {
			t.printStackTrace(pw);
			return sw.toString();
		}
		catch (IOException ignored) {
			return "Stacktrace failed";
		}
	}
}
