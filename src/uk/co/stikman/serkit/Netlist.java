package uk.co.stikman.serkit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.login.LoginContext;

public class Netlist implements Iterable<List<CellPin>> {

	private Circuit				circuit;
	private List<List<CellPin>>	lists		= new ArrayList<>();
	private List<BaseLogicUnit>	activeCells	= new ArrayList<>();

	public Netlist(Circuit circuit) {
		this.circuit = circuit;
	}

	public Circuit getCircuit() {
		return circuit;
	}

	public void addList(List<CellPin> list) {
		lists.add(list);
	}

	public List<List<CellPin>> getLists() {
		return lists;
	}

	@Override
	public Iterator<List<CellPin>> iterator() {
		return lists.iterator();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (List<CellPin> l : this) {
			String sep = "";
			for (CellPin pin : l) {
				sb.append(sep).append(pin.toString());
				sep = " - ";
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void setActiveCells(List<BaseLogicUnit> list) {
		this.activeCells = list;
	}

	public List<BaseLogicUnit> getActiveCells() {
		return activeCells;
	}

}
