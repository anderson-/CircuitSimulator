package com.falstad.circuit.elements;

    public class PTransistorElm extends TransistorElm {
	public PTransistorElm(int xx, int yy) { super(xx, yy, true); }
	public Class getDumpClass() { return TransistorElm.class; }
    }
