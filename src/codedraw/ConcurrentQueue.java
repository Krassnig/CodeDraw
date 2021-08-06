package codedraw;

class ConcurrentQueue<T> {
	public ConcurrentQueue() {
		this(16);
	}

	public ConcurrentQueue(int initialCapacity) {
		if (initialCapacity <= 2) throw new RuntimeException("Initial capacity must be greater than 2");

		list = createArray(initialCapacity);
	}

	private T[] list;
	private int offset = 0;
	private int length = 0;

	private Semaphore listLock = new Semaphore(1);
	private Semaphore listCount = new Semaphore(0);

	public void push(T element) {
		listLock.acquire();
		if (isFull()) {
			doubleCapacity();
		}
		pushInternal(element);
		listLock.release();

		listCount.release();
	}

	public T pop() {
		listCount.acquire();

		listLock.acquire();
		T result = popInternal();
		listLock.release();

		return result;
	}

	private void pushInternal(T element) {
		list[(offset + length++) % capacity()] = element;
	}

	private T popInternal() {
		T result = list[offset++ % capacity()];
		length--;
		if (offsetIsMultipleOfCapacity() || isEmpty()) offset = 0;
		return result;
	}

	private boolean offsetIsMultipleOfCapacity() {
		return offset % capacity() == 0;
	}

	private int capacity() {
		return list.length;
	}

	private boolean isEmpty() {
		return length == 0;
	}

	private boolean isFull() {
		return length == capacity();
	}

	private void doubleCapacity() {
		T[] newList = createArray(capacity() * 2);

		int firstPartCount = capacity() - offset;
		arrayCopy(list, offset, newList, 0, firstPartCount);
		arrayCopy(list, 0, newList, firstPartCount, offset);
		offset = 0;
	}

	private static <T> void arrayCopy(T[] source, int sourceOffset, T[] target, int targetOffset, int length) {
		System.arraycopy(source, sourceOffset, target, targetOffset, length);
	}

	private static <T> T[] createArray(int length) {
		return (T[]) new Object[length];
	}
}
