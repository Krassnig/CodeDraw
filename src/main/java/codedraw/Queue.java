package codedraw;

class Queue<T> {
	public Queue(int initialCapacity) {
		if (initialCapacity <= 0) throw new RuntimeException("Initial capacity must be larger than zero.");

		list = createGenericArray(initialCapacity);
	}

	private T[] list;
	private int offset = 0;
	private int length = 0;

	public T peek() {
		return list[offset];
	}

	public void push(T element) {
		if (isFull()) {
			doubleCapacity();
		}

		list[(offset + length++) % capacity()] = element;
	}

	public T pop() {
		if (length == 0) throw new RuntimeException("Cannot remove element from empty queue.");

		T result = list[offset];
		list[offset] = null; // explicitly set removed elements to null, easier to debug and maybe the garbage collector has an easier time cleaning up
		offset++;
		length--;
		offset = isEmpty() ? 0 : offset % capacity();
		return result;
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
		arrayCopy(list, 0, newList, firstPartCount, length - firstPartCount);
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
