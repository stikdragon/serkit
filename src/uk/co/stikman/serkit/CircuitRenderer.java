package uk.co.stikman.serkit;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import com.sun.openpisces.AlphaConsumer;

public class CircuitRenderer {

	private static final int				TILE_SIZE	= 16;

	private Map<Class<?>, BufferedImage>	images		= new HashMap<>();

	public CircuitRenderer() {
		super();
		images.put(InputUnit.class, readImage("tile_in.png"));
		images.put(OutputUnit.class, readImage("tile_out.png"));
		images.put(SimpleLogicUnit.class, readImage("tile.png"));
	}

	private BufferedImage readImage(String name) {
		try {
			try (InputStream is = getClass().getResourceAsStream(name)) {
				BufferedImage img = ImageIO.read(is);
				return img;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void render(Circuit c, String filename) {
		BufferedImage image = new BufferedImage(c.getSize() * TILE_SIZE, c.getSize() * TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();

		Set<Cell> active = new HashSet<>();
		Netlist nl = c.buildNetlist();
		for (Cell cell : nl.getActiveCells())
			active.add(cell);

		for (int j = 0; j < c.getSize(); ++j) {
			for (int i = 0; i < c.getSize(); ++i) {
				Cell cell = c.getCell(i, j);
				if (!active.contains(cell))
					g.setComposite(alpha);
				else
					g.setComposite(AlphaComposite.Src);
				if (cell instanceof Wire) {
					renderWire(g, (Wire) cell, i, j);
				} else if (cell instanceof BaseLogicUnit) {
					renderCell(g, (BaseLogicUnit) cell);
				}
			}
		}

		try {
			ImageIO.write(image, "PNG", new File(filename));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static final Color			LIGHTRED	= new Color(1.0f, 0.8f, 0.8f);
	private static final Color			LIGHTGREEN	= new Color(0.8f, 1.0f, 0.8f);
	private static final Color			LIGHTBLUE	= new Color(0.8f, 0.8f, 1.0f);
	private static final AlphaComposite	alpha		= AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f);

	private void renderCell(Graphics2D g, BaseLogicUnit cell) {
		Color red = Color.RED;
		Color green = Color.GREEN;

		renderImg(g, cell.getX(), cell.getY(), images.get(cell.getClass()));

		int ox = cell.getX() * TILE_SIZE;
		int oy = cell.getY() * TILE_SIZE;

		int r = cell.getRotation();
		for (int i = 0; i < 4; ++i) {
			Color c = i == 1 ? red : green;
			int x = 0;
			int y = 0;
			g.setColor(c);
			switch (r) {
			case 0: // north
				x = 7;
				break;
			case 1: // east
				x = 14;
				y = 7;
				break;
			case 2: // south
				x = 7;
				y = 14;
				break;
			case 3: // west
				x = 0;
				y = 7;
				break;
			}
			g.fillRect(ox + x, oy + y, 2, 2);

			++r;
			if (r >= 4)
				r = 0;
		}
	}

	private void renderImg(Graphics2D g, int i, int j, BufferedImage img) {
		g.drawImage(img, i * TILE_SIZE, j * TILE_SIZE, (i + 1) * TILE_SIZE, (j + 1) * TILE_SIZE, 0, 0, TILE_SIZE, TILE_SIZE, null);
	}

	private void renderWire(Graphics2D g, Wire wire, int i, int j) {
		int cfg = wire.getConfig();
		switch (cfg) {
		case 0:
		case 1:
		case 2:
		case 4:
		case 8:
			return; // nothing to draw for these 
		}

		int ox = i * TILE_SIZE;
		int oy = j * TILE_SIZE;

		g.setColor(LIGHTBLUE);
		if ((cfg & 0x1) != 0)  // north
			g.fillRect(7 + ox, oy, 2, 7);
		if ((cfg & 0x2) != 0)  // east
			g.fillRect(9 + ox, 7 + oy, 7, 2);
		if ((cfg & 0x4) != 0)  // south
			g.fillRect(7 + ox, 9 + oy, 2, 7);
		if ((cfg & 0x8) != 0)  // west
			g.fillRect(ox, 7 + oy, 7, 2);
		g.fillRect(7 + ox, 7 + oy, 2, 2);
	}

	public static void main(String[] args) {
		CircuitRenderer r = new CircuitRenderer();
		Circuit c = createSeedCircuit(8, new Random());

		r.render(c, "c:\\junk\\test.png");
		System.out.println(c);
	}

	private static Circuit createSeedCircuit(int size, Random rng) {
		Circuit c = new Circuit(size);
		for (int x = 0; x < size; ++x) {
			for (int y = 0; y < size; ++y) {
				if (rng.nextBoolean()) {
					Wire w = new Wire(rng.nextInt(16));
					c.setCell(x, y, w);
				} else {
					SimpleLogicUnit cell = new SimpleLogicUnit(rng.nextInt(4));
					float[] lkp = cell.getLookupTable();
					for (int i = 0; i < lkp.length; ++i)
						lkp[i] = rng.nextFloat();
					c.setCell(x, y, cell);
				}
			}

		}
		return c;
	}
}
