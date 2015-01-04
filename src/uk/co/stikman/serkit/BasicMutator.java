package uk.co.stikman.serkit;

import java.util.Random;

public class BasicMutator implements Mutator {

	@Override
	public void mutate(Random rng, Circuit circuit, int amount) {
		while (amount-- > 0) {
			if (rng.nextFloat() > ((float) amount) / 100.0f)
				continue;

			int x = rng.nextInt(circuit.getSize());
			int y = rng.nextInt(circuit.getSize());

			int op = rng.nextInt(20);
			if (op < 5) { // add
				int type = rng.nextInt(2);
				if (type == 0) { // wire
					circuit.setCell(x, y, new Wire(rng.nextInt(8)));
				} else { // logic unit
					if (type == 1) {
						circuit.setCell(x, y, new SimpleLogicUnit(rng.nextInt(4)));
					}
				}
			} else if (op < 6) { // remove
				circuit.setCell(x, y, null);
			} else { // modify
				Cell c = circuit.getCell(x, y);
				if (c == null)
					continue;

				if (c instanceof Wire) {
					Wire wire = (Wire) c;
					op = rng.nextInt(4);
					int n = 1 << op;
					wire.setConfig(wire.getConfig() ^ n);
				} else if (c instanceof SimpleLogicUnit) {
					SimpleLogicUnit slu = (SimpleLogicUnit) c;
					op = rng.nextInt(9);
					if (op < 8) {
						float val = rng.nextFloat() * 0.6f - 0.15f;
						slu.setLookupValue(op, slu.getLookupValue(op) + val);
					} else {
						int n = rng.nextInt(4) + 1;
						slu.setRotation((slu.getRotation() + n) % 4);
					}
				}
			}
		}
	}

}
