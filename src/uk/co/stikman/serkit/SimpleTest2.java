package uk.co.stikman.serkit;

import uk.co.stikman.serkit.scenario.Scenario;
import uk.co.stikman.serkit.scenario.SimpleScenario1;

public class SimpleTest2 {

	public static void main(String[] args) {
		Circuit c = new Circuit(3);
		InputUnit inp = new InputUnit();
		OutputUnit out = new OutputUnit();
		
		c.setCell(0, 1, inp);
		c.setCell(2, 1, out);
		c.setWire(1, 1, Direction.EAST, Direction.WEST, Direction.SOUTH);
		c.setInput(inp);
		c.setOutput(out);

		System.out.println(c.toString());

		Netlist nl = c.buildNetlist();
		System.out.println(nl.toString());
		for (Cell used : nl.getActiveCells())
			System.out.println(used.getCode());

		Scenario scenario = new SimpleScenario1();
		Simulator sim = new Simulator();
		sim.setCircuit(c);
		System.out.println(scenario.run(sim));
	}

}
