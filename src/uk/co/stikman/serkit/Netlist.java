package uk.co.stikman.serkit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.login.LoginContext;

public class Netlist implements Iterable<NetlistPath> {

	private Circuit				circuit;
	private List<NetlistPath>	lists		= new ArrayList<>();
	private List<Cell>	activeCells	= new ArrayList<>();

	public Netlist(Circuit circuit) {
		this.circuit = circuit;
	}

	public Circuit getCircuit() {
		return circuit;
	}

	public void addList(NetlistPath list) {
		lists.add(list);
	}

	public List<NetlistPath> getLists() {
		return lists;
	}

	@Override
	public Iterator<NetlistPath> iterator() {
		return lists.iterator();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (NetlistPath l : this) {
			String sep = "";
			for (CellPin pin : l) {
				sb.append(sep).append(pin.toString());
				sep = " - ";
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void setActiveCells(List<Cell> list) {
		this.activeCells = list;
	}

	public List<Cell> getActiveCells() {
		return activeCells;
	}

}
