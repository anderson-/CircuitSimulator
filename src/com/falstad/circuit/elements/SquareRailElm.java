package com.falstad.circuit.elements;

    public class SquareRailElm extends RailElm {
	public SquareRailElm(int xx, int yy) { super(xx, yy, WF_SQUARE); }
	public Class getDumpClass() { return RailElm.class; }
	public int getShortcut() { return 0; }
    }
