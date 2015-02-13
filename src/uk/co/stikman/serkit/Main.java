package uk.co.stikman.serkit;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import uk.co.stikman.serkit.Simulation.HistoryEntry;
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

	private int				seed		= 0;
	private Scenario		scenario	= new SimpleScenario1();
	private int				runtime		= 30000;

	private CircuitRenderer	renderer;

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
		final Simulation sim = new Simulation();
		sim.setSeed(seed);
		sim.setScenario(scenario);
		sim.setRuntime(runtime);
		sim.setSize(7);
		sim.setGenerationSize(50);
		sim.setHistorySkip(1000);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				sim.run();
			}
		});
		t.start();

		//
		// Run for 30s, print out the current head of the history queue
		// every 1s
		//
		long time = 30000;
		long lastT = System.currentTimeMillis();
		while (time > 0) {
			long dt = System.currentTimeMillis() - lastT;
			time -= dt;

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}

			int generationCount = sim.getGenerationCount();
			HistoryEntry c = null;
			synchronized (sim.getHistory()) {
				if (!sim.getHistory().isEmpty())
					c = sim.getHistory().get(sim.getHistory().size() - 1);
			}
			if (c != null) {
				System.out.println("Generation: " + generationCount + ".  Best: " + c.getScore() + " / " + c.getCombinedScore());
				c.getCircuit().renumber();
				System.out.println(c.getCircuit());
				drawCircuit(c.getCircuit(), "gen" + generationCount + ".png");
			}
		}

		sim.stop();
	}

	private void drawCircuit(Circuit c, String filename) {
		CircuitRenderer render = getRenderer();
		render.render(c, "rendered\\" + filename);
	}

	private CircuitRenderer getRenderer() {
		if (renderer == null)
			renderer = new CircuitRenderer();
		return renderer;
	}

}
