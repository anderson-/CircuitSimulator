package com.falstad.circuit;

import java.util.Vector;

public class CircuitNode {

    public int x, y;
    public Vector<CircuitNodeLink> links;
    public boolean internal;

    public CircuitNode() {
        links = new Vector<CircuitNodeLink>();
    }
}
