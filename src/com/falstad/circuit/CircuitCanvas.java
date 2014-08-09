package com.falstad.circuit;

import java.awt.*;

class CircuitCanvas extends Canvas {
    CircuitSimulator pg;
    CircuitCanvas(CircuitSimulator p) {
	pg = p;
    }
    @Override
    public Dimension getPreferredSize() {
	return new Dimension(300,400);
    }
    @Override
    public synchronized void update(Graphics g) {
	pg.updateCircuit(g);
    }
    @Override
    public void paint(Graphics g) {
	pg.updateCircuit(g);
    }
};
