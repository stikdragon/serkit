package uk.co.stikman.serkit;

public abstract class BaseLogicUnit extends Cell {
	private int			id;
	private int			rotation;
	private CellPin[]	pins	= new CellPin[4];

	public BaseLogicUnit() {
		super();
		for (int i = 0; i < 4; ++i)
			pins[i] = new CellPin(this, i == 1, i);
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

	/**
	 * Pin 0, 2 and 3 are inputs, 1 is the output (east)
	 * @param pin
	 * @return
	 */
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

	@Override
	public void copyFrom(Cell src) {
		super.copyFrom(src);
		BaseLogicUnit blu = (BaseLogicUnit) src;
		this.id = blu.id;
	}
	
	
	@Override
	public String getCode() {
		int n = getId();
		if (n < 10)
			return " " + n + " ";
		if (n < 100)
			return n + " ";
		if (n < 1000)
			return Integer.toString(n);
		return "*!*";
	}
	
	

}
