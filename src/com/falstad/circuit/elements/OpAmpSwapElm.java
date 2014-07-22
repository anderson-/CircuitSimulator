package com.falstad.circuit.elements;

    public class OpAmpSwapElm extends OpAmpElm {
	public OpAmpSwapElm(int xx, int yy) {
	    super(xx, yy);
	    flags |= FLAG_SWAP;
	}
	public Class getDumpClass() { return OpAmpElm.class; }
    }
