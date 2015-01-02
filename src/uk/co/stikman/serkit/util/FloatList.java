package uk.co.stikman.serkit.util;

public class FloatList {
	private float[]	list;
	private int		size;
	private int		increment	= 50;

	public FloatList() {
		list = new float[increment];
		size = 0;
	}

	public final void add(float val) {
		set(size, val);
	}

	public final void set(int index, float val) {
		if (index >= list.length) {
			float[] tmp = new float[index + 1 + increment];
			System.arraycopy(list, 0, tmp, 0, size);
			list = tmp;
		}
		if (index >= size) {
			int n = size;
			while (n < index) {
				list[n] = 0;
				++n;
			}
			size = index + 1;
		}
		list[index] = val;
	}

	public final float get(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException(Integer.toString(index));
		return list[index];
	}

	public final int size() {
		return size;
	}

	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

	public void clear() {
		size = 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; ++i) {
			if (i == size - 1)
				sb.append(list[i]);
			else
				sb.append(list[i]).append(", ");
		}
		return sb.toString();
	}

	public float last() {
		if (size == 0)
			throw new IndexOutOfBoundsException("List is empty");
		return list[size - 1];
	}

}
