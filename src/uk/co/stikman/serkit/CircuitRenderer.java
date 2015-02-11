package uk.co.stikman.serkit;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class CircuitRenderer {

	private static final int	TILE_SIZE	= 16;

	private BufferedImage		imgTileBase;
	private BufferedImage		imgTileBaseNone;

	public CircuitRenderer() {
		super();
		imgTileBase = readImage("tile.png");
		imgTileBaseNone = readImage("tile_none.png");
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
		for (int j = 0; j < c.getSize(); ++j) {
			for (int i = 0; i < c.getSize(); ++i) {
				g.drawImage(imgTileBase, i * TILE_SIZE, j * TILE_SIZE, (i + 1) * TILE_SIZE, (j + 1) * TILE_SIZE, 0, 0, TILE_SIZE, TILE_SIZE, null);
			}
		}

		try {
			ImageIO.write(image, "PNG", new File(filename));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static void main(String[] args) {
		CircuitRenderer r = new CircuitRenderer();
		Circuit c = createSeedCircuit(8, new Random());
		
		r.render(c, "c:\\junk\\test.png");
	}

	
	private static Circuit createSeedCircuit(int size, Random rng) {
		Circuit c = new Circuit(size);
		for (int x = 0; x < size; ++x) {
			for (int y = 0; y < size; ++y) {
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
