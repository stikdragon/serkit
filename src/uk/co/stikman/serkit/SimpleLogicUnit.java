package uk.co.stikman.serkit;

/**
 * By default these are OR gates
 * 
 * @author frenchd
 *
 */
public class SimpleLogicUnit extends BaseLogicUnit {

	private float[]					lookupTable	= new float[8];
	private final static float[]	DEFAULT_LUT	= { 0, 1, 1, 1, 1, 1, 1, 1 };
	private int inuseFlag = 1+2+4 + 8;

	public SimpleLogicUnit() {
		this(0);
	}

	/**
	 * @param rotate
	 */
	public SimpleLogicUnit(int rotate) {
		super(rotate);
		System.arraycopy(DEFAULT_LUT, 0, lookupTable, 0, lookupTable.length);
	}



	public float[] getLookupTable() {
		return lookupTable;
	}

	public void setLookupTable(float[] lookupTable) {
		if (lookupTable == null)
			throw new NullPointerException("Lookup table cannot be null");
		this.lookupTable = lookupTable;
	}

	public void setLookupValue(boolean a, boolean b, boolean c, float v) {
		int idx = 0;
		if (a)
			idx |= 1;
		if (b)
			idx |= 2;
		if (c)
			idx |= 4;
		lookupTable[idx] = v;
	}

	public void setLookupValue(int idx, float v) {
		lookupTable[idx] = v;
	}

	public float getLookupValue(boolean a, boolean b, boolean c) {
		int idx = 0;
		if (a)
			idx |= 1;
		if (b)
			idx |= 2;
		if (c)
			idx |= 4;
		return lookupTable[idx];
	}

	public float getLookupValue(int idx) {
		return lookupTable[idx];
	}

	@Override
	public void clock() {
		boolean a = getPin(0).getValue() > 0.5f;
		boolean b = getPin(2).getValue() > 0.5f;
		boolean c = getPin(3).getValue() > 0.5f;

		getPin(1).setValue(getLookupValue(a, b, c));
	}

	public void toDNA(DNAOutput output) {
		for (int i = 0; i < lookupTable.length; ++i)
			output.writeFloat(lookupTable[i]);
	}

	public void fromDNA(DNAInput input) {
		for (int i = 0; i < lookupTable.length; ++i)
			lookupTable[i] = input.readFloat();
	}

	public void createDefaultDNA(DNAOutput output) {
		for (int i = 0; i < DEFAULT_LUT.length; ++i)
			output.writeFloat(DEFAULT_LUT[i]);
	}

	@Override
	public Cell createClone() {
		SimpleLogicUnit r = new SimpleLogicUnit();
		r.copyFrom(this);
		return r;
	}

	@Override
	public void copyFrom(Cell src) {
		super.copyFrom(src);
		SimpleLogicUnit slu = (SimpleLogicUnit) src;
		for (int i = 0; i < lookupTable.length; ++i)
			lookupTable[i] = slu.lookupTable[i];
	}
	
	public boolean isPinConnected(int id) {
		switch (id) {
			case 0: return (inuseFlag & 0x1) == 0x1;
			case 1: throw new RuntimeException("Output pin is always in use");
			case 2: return (inuseFlag & 0x4) == 0x4; 
			case 3: return (inuseFlag & 0x8) == 0x8;
			default: throw new RuntimeException("Invalid pin: "+ id);
		}
	}

}
