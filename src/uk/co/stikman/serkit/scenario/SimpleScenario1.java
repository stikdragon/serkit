package uk.co.stikman.serkit.scenario;

import uk.co.stikman.serkit.Circuit;
import uk.co.stikman.serkit.Simulator;

public class SimpleScenario1 implements Scenario {

	private static final int	ITERATIONS		= 20;
	private static final int	STARTUP_TIME	= 10;

	@Override
	public float run(Simulator sim) {
		float result = 0.0f;
		Circuit c = sim.getCircuit();
		for (int i = 0; i < ITERATIONS; ++i) {
			float inp = (i % 2) == 0 ? 0 : 1;
			c.getInput().setValue(inp);
			sim.run();

			if (i >= STARTUP_TIME) {
				float out = 1.0f - c.getOutput().getValue();
				float expected = inp;
				//			System.out.println(String.format("%f / %f", expected, out));
				result += (1.0f - clamp(expected - out)) / (ITERATIONS - STARTUP_TIME);
			}
		}
		return result;
	}

	/**
	 * Abs(f) and clamps to the range 0..1
	 * 
	 * @param f
	 * @return
	 */
	private float clamp(float f) {
		if (f < 0.0f)
			f *= -1.0f;
		if (f > 1.0f)
			f = 1.0f;
		return f;

	}

}
