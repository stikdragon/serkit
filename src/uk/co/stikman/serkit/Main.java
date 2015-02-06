package uk.co.stikman.serkit;

import java.io.IOException;
import java.util.Random;

import uk.co.stikman.serkit.scenario.Scenario;
import uk.co.stikman.serkit.scenario.SimpleScenario1;
import uk.co.stikman.serkit.scenario.SimpleScenario2;

public class Main {

	private static final int	SIZE			= 7;
	private static final int	GENERATION_SIZE	= 50;

	public static void main(String[] args) {
		Main m = new Main();
		try {
			m.mainMenu();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int			seed		= 0;
	private Scenario	scenario	= new SimpleScenario1();
	private int			runtime		= 30000;

	private void mainMenu() throws IOException {
		int ch;
		boolean showmenu = true;
		while (true) {
			if (showmenu) {
				System.out.println("Serkit");
				System.out.println("======");
				System.out.println("Choose option:");
				System.out.println();
				System.out.println("  1. Change seed (current=" + seed + ")");
				System.out.println("  2. Change scenario (current=" + scenario.getClass().getSimpleName() + ")");
				System.out.println("  3. Change runtime (current=" + runtime + "s)");
				System.out.println("  R. Run");
				System.out.println("  Q. Quit");
			}
			showmenu = true;
			ch = System.in.read();
			ch = Character.toLowerCase(ch);
			switch (ch) {
				case 13:
				case 10:
					showmenu = false;
					continue;
				case 'q':
					return;
				case 'r':
					runSim(scenario);
					continue;
				default:
					System.err.println("Unknown: " + (char) ch);
			}
		}
	}

	private void runSim(Scenario scenario) {

		Mutator mutator = new BasicMutator();
		Simulator sim = new Simulator();
		Generation previous = new Generation();
		Random rng = new Random(0);

		//
		// Make a few completely random ones that are entirely gates
		//
		for (int i = 0; i < 5; ++i) {
			Circuit c = createSeedCircuit(rng);
			previous.add(c, 0.0f);
			c.renumber();
		}

		int generationCount = 0;
		long start = System.currentTimeMillis();
		while (true) {
			long d = System.currentTimeMillis();
			if (d - start > runtime)
				break;

			previous.sort(rng.nextInt(10) == 0);
			previous.keep(2 + rng.nextInt(4));

			++generationCount;
			if (generationCount % 1000 == 0) {
				System.out.println("Generation: " + generationCount + ".  Best: " + previous.get(0).getScore() + " / " + previous.get(0).getCombinedScore());
				Circuit c = previous.get(0).getCircuit();
				c.renumber();
				System.out.println(c);
			}

			//
			// Create a new generation from the best of the previous one, and add together
			//
			Generation newGen = new Generation();
			for (int i = 0; i < GENERATION_SIZE; ++i) {
				Circuit c = new Circuit(previous.get(rng.nextInt(previous.size())).getCircuit());
				if (rng.nextInt(25) == 0)
					mutator.mutate(rng, c, 100);
				if (rng.nextInt(100) == 0)
					mutator.mutate(rng, c, 10000);
				mutator.mutate(rng, c, 10);

				InputUnit inp = new InputUnit();
				OutputUnit out = new OutputUnit(0);
				c.setCell(SIZE - 1, SIZE / 2, out);
				c.setCell(0, SIZE / 2, inp);
				c.setInput(inp);
				c.setOutput(out);

				sim.setCircuit(c);
				float f = scenario.run(sim);
				newGen.add(c, f);
			}
			newGen.addAll(previous);
			previous = newGen;

		}
	}

	private static Circuit createSeedCircuit(Random rng) {
		Circuit c = new Circuit(SIZE);
		for (int x = 0; x < SIZE; ++x) {
			for (int y = 0; y < SIZE; ++y) {
				SimpleLogicUnit cell = new SimpleLogicUnit(rng.nextInt(4));
				float[] lkp = cell.getLookupTable();
				for (int i = 0; i < lkp.length; ++i)
					lkp[i] = rng.nextFloat();
				c.setCell(x, y, cell);
			}
		}
		return c;
	}

}
