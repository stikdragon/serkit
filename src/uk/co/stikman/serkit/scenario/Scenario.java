package uk.co.stikman.serkit.scenario;

import uk.co.stikman.serkit.Simulator;

public interface Scenario {


	/**
	 * returns a score from 0..1 for how well it did
	 * 
	 * @param sim
	 * @return
	 */
	float run(Simulator sim);

}
