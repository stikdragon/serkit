package uk.co.stikman.serkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Generation implements Iterable<Generation.Entry> {
	public class Entry {
		private Circuit	circuit;
		private float	score;

		/**
		 * @param circuit
		 * @param score
		 */
		public Entry(Circuit circuit, float score) {
			super();
			this.circuit = circuit;
			this.score = score;
		}

		public Circuit getCircuit() {
			return circuit;
		}

		public float getScore() {
			return score;
		}

	}

	private List<Entry>	entries	= new ArrayList<>();

	public int size() {
		return entries.size();
	}

	public void sort() {
		Collections.sort(entries, new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				return Float.compare(b.score, a.score);
			}
		});
	}

	/**
	 * Keeps only the first n results
	 * 
	 * @param n
	 */
	public void keep(int n) {
		ArrayList<Entry> tmp = new ArrayList<Generation.Entry>();
		for (Entry e : entries)
			if (n-- > 0)
				tmp.add(e);
		entries = tmp;
	}

	@Override
	public Iterator<Entry> iterator() {
		return entries.iterator();
	}

	public void add(Circuit c, float score) {
		entries.add(new Entry(c, score));
	}
	
	public Entry get(int idx) {
		return entries.get(idx);
	}

}
