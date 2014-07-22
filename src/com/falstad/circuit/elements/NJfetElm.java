package com.falstad.circuit.elements;

public class NJfetElm extends JfetElm {

    public NJfetElm(int xx, int yy) {
        super(xx, yy, false);
    }

    public Class getDumpClass() {
        return JfetElm.class;
    }
}
