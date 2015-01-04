package uk.co.stikman.serkit;

public class InputUnit extends BaseLogicUnit {

	public InputUnit() {
		super();
	}

	/**
	 * @param rotate
	 */
	public InputUnit(int rotate) {
		super(rotate);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCode() {
		return " I ";
	}

	@Override
	public void clock() {

	}

	public void setValue(float val) {
		getPin(1).setValue(val);
	}

	@Override
	public Cell createClone() {
		InputUnit r = new InputUnit();
		r.copyFrom(this);
		return r;
	}


}
