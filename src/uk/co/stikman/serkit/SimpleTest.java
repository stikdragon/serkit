package uk.co.stikman.serkit;

import uk.co.stikman.serkit.scenario.Scenario;
import uk.co.stikman.serkit.scenario.SimpleScenario1;

public class SimpleTest {

	public static void main(String[] args) {
		Circuit c = new Circuit(4);
		InputUnit inp = new InputUnit();
		c.setCell(0, 1, inp);
		c.setWire(1, 1, Direction.EAST, Direction.WEST, Direction.SOUTH);
		c.setWire(2, 1, Direction.EAST, Direction.WEST);
		c.setCell(1, 2, new SimpleLogicUnit(3));
		c.setCell(2, 3, new SimpleLogicUnit(2));
		c.setWire(1, 3, Direction.NORTH, Direction.EAST);
		OutputUnit out = new OutputUnit(0);
		c.setCell(3, 1, out);
		c.setCell(2, 0, new SimpleLogicUnit());
		c.setInput(inp);
		c.setOutput(out);

		System.out.println(c.toString());

		Netlist nl = c.buildNetlist();
		System.out.println(nl.toString());
		for (BaseLogicUnit used : nl.getActiveCells())
			System.out.println(used.getCode());

		Scenario scenario = new SimpleScenario1();
		Simulator sim = new Simulator(c);
		System.out.println(scenario.run(sim));
	}

}
