package codedraw.events;

import codedraw.Semaphore;

class ConcurrentQueue<T> {
	public ConcurrentQueue() {
		this(16);
	}

	public ConcurrentQueue(int initialCapacity) {
		if (initialCapacity <= 0) throw new RuntimeException("Initial capacity must be larger than zero.");

		list = createGenericArray(initialCapacity);
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

	public T peek(){
		listCount.acquire();
		listLock.acquire();
		T result = peekInternal();
		listLock.release();
		listCount.release();
		return result;
	}

	private T peekInternal() {
		return list[offset % capacity()];
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
		T[] newList = createGenericArray(capacity() << 1);

		int firstPartCount = capacity() - offset;
		arrayCopy(list, offset, newList, 0, firstPartCount);
		arrayCopy(list, 0, newList, firstPartCount, offset);
		list = newList;
		offset = 0;
	}

	private static <T> void arrayCopy(T[] source, int sourceOffset, T[] target, int targetOffset, int length) {
		System.arraycopy(source, sourceOffset, target, targetOffset, length);
	}

	@SuppressWarnings("unchecked")
	private static <T> T[] createGenericArray(int length) {
		return (T[]) new Object[length];
	}
}
