package com.falstad.circuit.elements;

public class CC2NegElm extends CC2Elm {

    public CC2NegElm(int xx, int yy) {
        super(xx, yy, -1);
    }

    @Override
    public Class getDumpClass() {
        return CC2Elm.class;
    }
}
