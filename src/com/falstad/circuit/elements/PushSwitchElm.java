package com.falstad.circuit.elements;

    public class PushSwitchElm extends SwitchElm {
	public PushSwitchElm(int xx, int yy) { super(xx, yy, true); }
	public Class getDumpClass() { return SwitchElm.class; }
	public int getShortcut() { return 0; }
    }
