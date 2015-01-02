package uk.co.stikman.serkit;

public class CellPin {

	private final BaseLogicUnit	cell;
	private final boolean		output;
	private final int			id;
	private float				value;

	public CellPin(BaseLogicUnit cell, boolean output, int id) {
		this.cell = cell;
		this.output = output;
		this.id = id;
	}

	public BaseLogicUnit getCell() {
		return cell;
	}

	public boolean isOutput() {
		return output;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		if (output)
			return cell.toString() + ":out" + id;
		else
			return cell.toString() + ":in" + id;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

}
