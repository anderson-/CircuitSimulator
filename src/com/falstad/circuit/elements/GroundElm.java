package com.falstad.circuit.elements;

import com.falstad.circuit.CircuitElm;
import java.awt.*;
import java.util.StringTokenizer;

public class GroundElm extends CircuitElm {

    public GroundElm(int xx, int yy) {
        super(xx, yy);
    }

    public GroundElm(int xa, int ya, int xb, int yb, int f,
            StringTokenizer st) {
        super(xa, ya, xb, yb, f);
    }

    public int getDumpType() {
        return 'g';
    }

    public int getPostCount() {
        return 1;
    }

    public void draw(Graphics g) {
        setVoltageColor(g, 0);
        drawThickLine(g, point1, point2);
        int i;
        for (i = 0; i != 3; i++) {
            int a = 10 - i * 4;
            int b = i * 5; // -10;
            interpPoint2(point1, point2, ps1, ps2, 1 + b / dn, a);
            drawThickLine(g, ps1, ps2);
        }
        doDots(g);
        interpPoint(point1, point2, ps2, 1 + 11. / dn);
        setBbox(point1, ps2, 11);
        drawPost(g, x, y, nodes[0]);
    }

    public void setCurrent(int x, double c) {
        current = -c;
    }

    public void stamp() {
        sim.stampVoltageSource(0, nodes[0], voltSource, 0);
    }

    public double getVoltageDiff() {
        return 0;
    }

    public int getVoltageSourceCount() {
        return 1;
    }

    public void getInfo(String arr[]) {
        arr[0] = "ground";
        arr[1] = "I = " + getCurrentText(getCurrent());
    }

    public boolean hasGroundConnection(int n1) {
        return true;
    }

    public int getShortcut() {
        return 'g';
    }
}
