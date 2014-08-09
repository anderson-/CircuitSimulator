package com.falstad.circuit.elements;

import java.awt.*;
import java.util.StringTokenizer;

public class NandGateElm extends AndGateElm {

    public NandGateElm(int xx, int yy) {
        super(xx, yy);
    }

    public NandGateElm(int xa, int ya, int xb, int yb, int f,
            StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
    }

    boolean isInverting() {
        return true;
    }

    public String getGateName() {
        return "NAND gate";
    }

    public int getDumpType() {
        return 151;
    }

    public int getShortcut() {
        return '@';
    }
}
