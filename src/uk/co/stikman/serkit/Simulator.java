package uk.co.stikman.serkit;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
	private Circuit	circuit;
	private int		step;
	private Netlist	netlist;
	private List<BaseLogicUnit>	activeLogicUnits;

	/**
	 * @param circuit
	 */
	public Simulator() {
		super();
		step = 0;
	}

	public void setCircuit(Circuit c) {
		this.circuit = c;
		this.netlist = circuit.buildNetlist();
		this.activeLogicUnits = new ArrayList<>();
		for (Cell cell : netlist.getActiveCells())
			if (cell instanceof BaseLogicUnit)
				activeLogicUnits.add((BaseLogicUnit) cell);
		circuit.init();
	}

	
	public Circuit getCircuit() {
		return circuit;
	}

	public int getStep() {
		return step;
	}

	public void run() {
		++step;

		//
		// For each path in the netlist we get the outputs and 
		// assign them to the inputs, once done clock the set of active
		// cells
		//
		for (NetlistPath net : netlist) {
			float val = 0.0f;
			int count = 0;
			for (CellPin pin : net) {
				if (pin.isOutput()) {
					val += pin.getValue();
					++count;
				}
			}
			if (count > 0)
				val /= count;
			else
				val = Float.NaN; 
			

			for (CellPin pin : net)
				if (!pin.isOutput())
					pin.setValue(val);
		}
		
		for (BaseLogicUnit cell: activeLogicUnits)
			cell.clock();
		
		//
		// Set all pins back to 0.5, the next step will update their values
		//
		for (BaseLogicUnit cell : activeLogicUnits) 
			cell.initialiseInputPins(0.5f);
		

	}

}
