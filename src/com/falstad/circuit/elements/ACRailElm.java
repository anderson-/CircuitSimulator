package com.falstad.circuit.elements;

public class ACRailElm extends RailElm {

    public ACRailElm(int xx, int yy) {
        super(xx, yy, WF_AC);
    }

    @Override
    public Class getDumpClass() {
        return RailElm.class;
    }

    @Override
    public int getShortcut() {
        return 0;
    }
}
