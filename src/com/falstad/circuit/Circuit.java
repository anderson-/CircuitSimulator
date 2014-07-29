package com.falstad.circuit;

// Circuit.java (c) 2005,2008 by Paul Falstad, www.falstad.com
import java.awt.Dimension;
import javax.swing.JFrame;

public class Circuit {

    public static void main(String args[]) {
        try {
            CircuitSimulator ogf = new CircuitSimulator();
            JFrame window = new JFrame();
            ogf.setContainer(window.getContentPane());
            ogf.init();
            window.setJMenuBar(ogf.getGUI().createGUI(true));
            ogf.posInit();
            window.pack();
            window.setSize(new Dimension(600, 600));
            window.setVisible(true);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
};
