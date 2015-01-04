package uk.co.stikman.serkit;

public abstract class Cell {
	private int	x;
	private int	y;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @param x
	 * @param y
	 */
	public Cell() {
		super();
	}

	public abstract String getCode();

	public abstract Cell createClone();
	
	public void copyFrom(Cell src) {
		this.x = src.x;
		this.y = src.y;
	}

}
