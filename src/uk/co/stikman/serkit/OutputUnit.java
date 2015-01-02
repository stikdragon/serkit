package uk.co.stikman.serkit;

public class OutputUnit extends BaseLogicUnit {

	public OutputUnit() {
		super();
	}

	public OutputUnit(int rotate) {
		super(rotate);
	}

	@Override
	public String getCode() {
		return " O ";
	}

	@Override
	public void clock() {

	}

	public float getValue() {
//		return (getPin(0).getValue() + getPin(1).getValue() + getPin(2).getValue()) / 3.0f;
		return getPin(1).getValue();
	}

	@Override
	public Cell createClone() {
		return new OutputUnit(getRotation());
	}

}
