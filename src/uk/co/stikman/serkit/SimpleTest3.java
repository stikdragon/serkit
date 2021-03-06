package uk.co.stikman.serkit;

import java.util.Random;

import uk.co.stikman.serkit.scenario.Scenario;
import uk.co.stikman.serkit.scenario.SimpleScenario1;

public class SimpleTest3 {

	public static void main(String[] args) {
		Circuit c = new Circuit(3);

		Mutator mutator = new BasicMutator();
		Random rng = new Random(0);
		mutator.mutate(rng, c, 100000);
		c.setWire(1, 1, Direction.EAST, Direction.WEST, Direction.SOUTH);
		c.renumber();
		
		InputUnit inp = new InputUnit();
		OutputUnit out = new OutputUnit();
		c.setCell(0, 1, inp);
		c.setCell(2, 1, out);
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
