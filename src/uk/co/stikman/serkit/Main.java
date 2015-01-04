package uk.co.stikman.serkit;

import java.util.Random;

import uk.co.stikman.serkit.scenario.Scenario;
import uk.co.stikman.serkit.scenario.SimpleScenario1;

public class Main {

	private static final int	SIZE			= 7;
	private static final int	GENERATION_SIZE	= 50;

	public static void main(String[] args) {

		Mutator mutator = new BasicMutator();
		Scenario scenario = new SimpleScenario1();
		Simulator sim = new Simulator();
		Generation previous = new Generation();
		Random rng = new Random(0);

		//
		// Make a few completely random ones to seed it
		//
		for (int i = 0; i < 5; ++i) {
			Circuit c = new Circuit(SIZE);
			mutator.mutate(rng, c, 100000);
			previous.add(c, 0.0f);
			c.renumber();
		}

		int generationCount = 0;
		long start = System.currentTimeMillis();
		while (true) {
			long d = System.currentTimeMillis();
			if (d - start > 50000)
				break;

			previous.sort(rng.nextInt(10) == 0);
			previous.keep(2 + rng.nextInt(4));

			++generationCount;
			if (generationCount % 1000 == 0) {
				System.out.println("Generation: " + generationCount + ".  Best: " + previous.get(0).getScore() + " / " +  previous.get(0).getCombinedScore());
				Circuit c = previous.get(0).getCircuit();
				c.renumber();
				System.out.println(c);
			}

			//
			// Create a new generation from the best of the previous one
			//
			Generation current = new Generation();
			for (int i = 0; i < GENERATION_SIZE; ++i) {
				Circuit c = new Circuit(previous.get(rng.nextInt(previous.size())).getCircuit());
				mutator.mutate(rng, c, 10);

				InputUnit inp = new InputUnit();
				OutputUnit out = new OutputUnit(0);
				c.setCell(SIZE - 1, SIZE / 2, out);
				c.setCell(0, SIZE / 2, inp);
				c.setInput(inp);
				c.setOutput(out);

				sim.setCircuit(c);
				float f = scenario.run(sim);
				current.add(c, f);
			}

			previous = current;

		}
	}

}
