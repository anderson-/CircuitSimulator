package com.falstad.circuit.elements;

public class PJfetElm extends JfetElm {

    public PJfetElm(int xx, int yy) {
        super(xx, yy, true);
    }

    public Class getDumpClass() {
        return JfetElm.class;
    }
}
