package uk.co.stikman.serkit;

import java.util.Random;

import uk.co.stikman.serkit.scenario.Scenario;
import uk.co.stikman.serkit.scenario.SimpleScenario1;

public class Main {

	private static final int	SIZE	= 8;
	private static final int	GENERATION_SIZE	= 50;


	public static void main(String[] args) {
		Circuit c = new Circuit(SIZE);

		Netlist nl = c.buildNetlist();
		System.out.println(nl.toString());
		for (BaseLogicUnit used : nl.getActiveCells())
			System.out.println(used.getCode());

		Mutator mutator = new BasicMutator();
		Scenario scenario = new SimpleScenario1();
		Simulator sim = new Simulator(c);
		Generation previous = new Generation();
		previous.add(c, 0.0f);
		
		Random rng = new Random();
		
		long start = System.currentTimeMillis();
		while (true) {
			long d = System.currentTimeMillis();
			if (d - start > 5000)
				break;
			
			previous.sort();
			previous.keep(5);
			
			//
			// Create a new generation from the best of the previous one
			//
			Generation current = new Generation();
			for (int i = 0; i < GENERATION_SIZE; ++i) {
				c = new Circuit(previous.get(rng.nextInt(previous.size())).getCircuit());
				mutator.mutate(c);
				
				InputUnit inp = new InputUnit();
				OutputUnit out = new OutputUnit(0);
				c.setCell(SIZE - 1, SIZE / 2, out);
				c.setCell(0, SIZE / 2, inp);
				c.setInput(inp);
				c.setOutput(out);

				System.out.println(scenario.run(sim));
				sim.setCircuit(c);
				current.add(c, scenario.run(sim));
			}
		}
	}

}
