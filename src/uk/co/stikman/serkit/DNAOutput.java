package uk.co.stikman.serkit;

import uk.co.stikman.serkit.util.FloatList;

public class DNAOutput {

	private FloatList	list	= new FloatList();

	public void writeFloat(float f) {
		list.add(f);
	}

	public FloatList getList() {
		return list;
	}

}
