package com.falstad.circuit.elements;

public class ACVoltageElm extends VoltageElm {

    public ACVoltageElm(int xx, int yy) {
        super(xx, yy, WF_AC);
    }

    @Override
    public Class getDumpClass() {
        return VoltageElm.class;
    }
}
