package com.falstad.circuit.elements;

    public class NTransistorElm extends TransistorElm {
	public NTransistorElm(int xx, int yy) { super(xx, yy, false); }
	public Class getDumpClass() { return TransistorElm.class; }
    }
