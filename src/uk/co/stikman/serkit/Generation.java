package uk.co.stikman.serkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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

		/**
		 * Adjusted for complexity
		 * 
		 * @return
		 */
		public float getCombinedScore() {
			return score - (0.2f * (1.0f - circuit.getComplexity()));
		}

		@Override
		public String toString() {
			return "Entry [circuit=..., score=" + score + "]";
		}

	}

	private List<Entry>	entries	= new ArrayList<>();

	public int size() {
		return entries.size();
	}

	public void sort(final boolean considerComplexity) {
		Collections.sort(entries, new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				float fa = a.score;
				float fb = b.score;
				if (considerComplexity) {
					fa = (0.8f * fa) + (0.2f * (1.0f - a.circuit.getComplexity())); // a high complexity lowers a circuit's score
					fb = (0.8f * fb) + (0.2f * (1.0f - b.circuit.getComplexity()));
				}
				return Float.compare(fb, fa);
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

	@Override
	public String toString() {
		return "Generation Size=" + entries.size() + " - [entries=" + entries + "]";
	}

}
