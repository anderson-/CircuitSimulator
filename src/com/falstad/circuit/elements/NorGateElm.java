package com.falstad.circuit.elements;

import java.awt.*;
import java.util.StringTokenizer;

public class NorGateElm extends OrGateElm {

    public NorGateElm(int xx, int yy) {
        super(xx, yy);
    }

    public NorGateElm(int xa, int ya, int xb, int yb, int f,
            StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
    }

    public String getGateName() {
        return "NOR gate";
    }

    boolean isInverting() {
        return true;
    }

    public int getDumpType() {
        return 153;
    }

    public int getShortcut() {
        return '#';
    }
}
