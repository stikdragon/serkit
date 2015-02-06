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
	private boolean		rebuildComplexity	= true;
	private float		complexity			= 0.0f;

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
		rebuildComplexity = true;
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
		rebuildComplexity = true;
		if (c == null)
			return;
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
		// A bit of a bodge, but cba to fix the above logic: find any logic 
		// units which have another one adjacent to their outputs, these form
		// their own little netlists (ie. connected components which don't 
		// have a wire between them)
		//
		for (Cell c : cells) {
			if (c == null || !(c instanceof BaseLogicUnit))
				continue;
			BaseLogicUnit blu = (BaseLogicUnit) c;
			int dx = 0;
			int dy = 0;
			switch (blu.getRotation()) {
				case 0:
					dx = 1;
					break;
				case 1:
					dy = 1;
					break;
				case 2:
					dx = -1;
					break;
				case 3:
					dy = -1;
					break;
			}
			Cell c2 = getCell(c.getX() + dx, c.getY() + dy);
			if (c2 != null && c2 instanceof BaseLogicUnit) {
				BaseLogicUnit blu2 = (BaseLogicUnit) c2;
				List<CellPin> list = new ArrayList<CellPin>();
				list.add(blu.getPin(1));
				list.add(blu2.getPin((blu.getRotation() + 3 + blu2.getRotation()) % 4));
				res.addList(list);
			}
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
			int pin = (camefromdir.ordinal() + 2 + unit.getRotation()) % 4;
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
		rebuildComplexity = true;
	}

	public void setInput(InputUnit inp) {
		this.input = inp;
		rebuildComplexity = true;
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
				((BaseLogicUnit) cell).initialiseInputPins(Float.NaN);
	}

	/**
	 * Renumbers all the logic units, starting from 0. Only really useful if
	 * you're displaying them, otherwise it's a waste of time
	 * 
	 */
	public void renumber() {
		int id = 0;
		for (Cell c : cells)
			if (c != null && c instanceof BaseLogicUnit)
				((BaseLogicUnit) c).setId(++id);
	}

	/**
	 * Returns a float between 0 and 1, 1 being the most complex circuit
	 * possible. This trivial implementation simply weights each cell with a
	 * value (3 for a gate, 1 for a wire) and divides by the max possible score
	 * 
	 * @return
	 */
	public float getComplexity() {
		if (rebuildComplexity) {
			rebuildComplexity = false;
			int score = 0;
			for (Cell c : cells) {
				if (c != null) {
					if (c instanceof Wire)
						++score;
					else
						score += 3;
				}
			}
			complexity = (float) score / (3 * size * size);
		}
		return complexity;
	}

}
