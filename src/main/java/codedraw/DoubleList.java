package codedraw;

class DoubleList {
	public DoubleList() { }

	private double[] list = new double[16];
	private int count = 0;

	public DoubleList add(double... values) {
		while (count + values.length > capacity()) {
			doubleCapacity();
		}

		for (int i = 0; i < values.length; i++) {
			list[count++] = values[i];
		}

		return this;
	}

	private void doubleCapacity() {
		double[] newList = new double[capacity() * 2];
		System.arraycopy(list, 0, newList, 0, count);
		list = newList;
	}

	private int capacity() {
		return list.length;
	}

	public double[] toArray() {
		double[] result = new double[count];
		System.arraycopy(list, 0, result, 0, count);
		return result;
	}
}
