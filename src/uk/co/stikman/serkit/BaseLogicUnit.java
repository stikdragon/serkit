package uk.co.stikman.serkit;

public abstract class BaseLogicUnit extends Cell {
	private int			id;
	private int			rotation;
	private CellPin[]	pins	= new CellPin[4];

	public BaseLogicUnit() {
		super();
		for (int i = 0; i < 4; ++i)
			pins[i] = new CellPin(this, i == 3, i);
	}

	public BaseLogicUnit(int rotate) {
		this();
		this.rotation = rotate;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public CellPin getPin(int pin) {
		return pins[pin];
	}

	@Override
	public String toString() {
		return getCode().trim();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public abstract void clock();

	public void initialiseInputPins(float val) {
		for (CellPin p : pins)
			if (!p.isOutput())
				p.setValue(val);
	}

}
