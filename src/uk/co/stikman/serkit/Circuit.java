package uk.co.stikman.serkit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Circuit {
	private int			size;

	private Cell[]		cells;

	private int			nextCellId;

	private OutputUnit	output;

	private InputUnit	input;

	/**
	 * @param size
	 * @param cells
	 */
	public Circuit(int size) {
		super();
		this.size = size;
		cells = new Cell[size * size];
	}

	public Circuit(Circuit copy) {
		this(copy.getSize());
		//
		// Make copy of all cells
		//
		for (int i = 0; i < size * size; ++i) {
			Cell c = copy.cells[i];
			if (c != null) 
				cells[i] = c.createClone();
		}

	}

	public int getSize() {
		return size;
	}

	/**
	 * Returns <code>null</code> for invalid references
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Cell getCell(int x, int y) {
		if (x < 0 || y < 0 || x >= size || y >= size)
			return null;
		return cells[y * size + x];
	}

	public void setCell(int x, int y, Cell c) {
		cells[y * size + x] = c;
		c.setX(x);
		c.setY(y);
		if (c instanceof BaseLogicUnit)
			((BaseLogicUnit) c).setId(++nextCellId);
	}

	public Netlist buildNetlist() {
		Netlist res = new Netlist(this);
		//
		// For each bit of path search its extents to make a netlist for it
		//
		Set<Wire> open = new HashSet<>();
		for (Cell c : cells)
			if (c != null && c instanceof Wire)
				open.add((Wire) c);

		while (!open.isEmpty()) {
			Wire cur = open.iterator().next();
			List<CellPin> list = new ArrayList<>();
			follow(open, cur, list, null);
			res.addList(list);
		}

		//
		// Get list of connected components.  Anything that doesn't have its output 
		// in a netlist is considered to be unused.  
		// TODO: we can improve this so that only things that are connected to the 
		// output somehow are considered
		//
		Set<BaseLogicUnit> used = new HashSet<>();
		for (List<CellPin> list : res)
			for (CellPin pin : list)
				if (pin.isOutput())
					used.add(pin.getCell());
		res.setActiveCells(new ArrayList<BaseLogicUnit>(used));
		return res;
	}

	private void follow(Set<Wire> open, Cell cell, List<CellPin> result, Direction camefromdir) {
		if (cell == null)
			return;// empty cell
		if (cell instanceof Wire) {
			Wire wire = (Wire) cell;
			if (!open.contains(wire)) // been here before
				return;
			open.remove(wire);
			if (wire.isNorth())
				follow(open, getCell(wire.getX(), wire.getY() - 1), result, Direction.NORTH);
			if (wire.isEast())
				follow(open, getCell(wire.getX() + 1, wire.getY()), result, Direction.EAST);
			if (wire.isSouth())
				follow(open, getCell(wire.getX(), wire.getY() + 1), result, Direction.SOUTH);
			if (wire.isWest())
				follow(open, getCell(wire.getX() - 1, wire.getY()), result, Direction.WEST);

		} else if (cell instanceof BaseLogicUnit) {
			//
			// Pay attention to the direction we came from to get the pin on this cell. There's
			// also the rotation of the cell to consider
			//
			BaseLogicUnit unit = (BaseLogicUnit) cell;
			int pin = (camefromdir.ordinal() + unit.getRotation()) % 4;
			result.add(unit.getPin(pin));
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < size; ++y) {
			for (int x = 0; x < size; ++x) {
				Cell c = getCell(x, y);
				if (c == null)
					sb.append(" . ");
				else
					sb.append(c.getCode());
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public void setWire(int x, int y, int config) {
		setCell(x, y, new Wire(config));
	}

	public void setWire(int x, int y, Direction... dirs) {
		int config = 0;
		for (Direction d : dirs) {
			switch (d) {
			case NORTH:
				config |= 1;
				break;
			case EAST:
				config |= 2;
				break;
			case SOUTH:
				config |= 4;
				break;
			case WEST:
				config |= 8;
				break;
			}
		}
		setWire(x, y, config);
	}

	public void setOutput(OutputUnit out) {
		this.output = out;
	}

	public void setInput(InputUnit inp) {
		this.input = inp;
	}

	public OutputUnit getOutput() {
		return output;
	}

	public InputUnit getInput() {
		return input;
	}

	public void init() {
		for (Cell cell : cells)
			if (cell != null && cell instanceof BaseLogicUnit)
				((BaseLogicUnit) cell).initialiseInputPins(0.5f);
	}

}
