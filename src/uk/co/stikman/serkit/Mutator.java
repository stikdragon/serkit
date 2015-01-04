package uk.co.stikman.serkit;

import java.util.Random;

public interface Mutator {

	void mutate(Random rng, Circuit c, int amount);
	
	

}
