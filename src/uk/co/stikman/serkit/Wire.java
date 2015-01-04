package uk.co.stikman.serkit;

public class Wire extends Cell {
	private int	config;

	public Wire(boolean e, boolean w, boolean s, boolean n) {
		super();
		config = 0;
		setEast(e);
		setWest(w);
		setSouth(s);
		setNorth(n);
	}

	public Wire(int config) {
		super();
		this.config = config;
	}

	public Wire() {
		super();
	}

	private void setEast(boolean e) {
		if (e)
			config |= 2;
		else
			config &= ~2;
	}

	private void setNorth(boolean n) {
		if (n)
			config |= 1;
		else
			config &= ~1;
	}

	private void setSouth(boolean s) {
		if (s)
			config |= 4;
		else
			config &= ~4;
	}

	private void setWest(boolean w) {
		if (w)
			config |= 8;
		else
			config &= ~8;
	}

	public int getConfig() {
		return config;
	}

	public void setConfig(int config) {
		this.config = config;
	}

	public boolean isEast() {
		return (config & 2) != 0;
	}

	public boolean isWest() {
		return (config & 8) != 0;
	}

	public boolean isNorth() {
		return (config & 1) != 0;
	}

	public boolean isSouth() {
		return (config & 4) != 0;
	}

	@Override
	public String getCode() {
		switch (config) {
		case 0:
			return "   ";
		case 1:
			return "   ";
		case 2:
			return "   ";
		case 3:
			return " └─";
		case 4:
			return "   ";
		case 5:
			return " │ ";
		case 6:
			return " ┌─";
		case 7:
			return " ├─";
		case 8:
			return "   ";
		case 9:
			return "─┘ ";
		case 10:
			return "───";
		case 11:
			return "─┴─";
		case 12:
			return "─┐ ";
		case 13:
			return "─┤ ";
		case 14:
			return "─┬─";
		case 15:
			return "─┼─";
		}
		return "???";
	}

	@Override
	public String toString() {
		return "(" + getX() + "," + getY() + ")";
	}

	@Override
	public Cell createClone() {
		Wire r = new Wire();
		r.copyFrom(this);
		return r;
	}

	@Override
	public void copyFrom(Cell src) {
		super.copyFrom(src);
		Wire w = (Wire) src;
		this.config = w.config;
	}

}
