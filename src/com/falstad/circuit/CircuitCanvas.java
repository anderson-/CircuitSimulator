package com.falstad.circuit;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import javax.swing.JFrame;
import javax.swing.JPanel;

class CircuitCanvas {

    private static final boolean debugFrame = false;

    private boolean runOnCanvas;

    private Canvas canvas;
    private CircuitSimulator cs;

    long sleep = 0;
    boolean up = true;

    CircuitCanvas(CircuitSimulator p, boolean runOnCanvas) {
        this.runOnCanvas = runOnCanvas;
        cs = p;
        if (runOnCanvas) {
            canvas = new Canvas() {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(300, 400);
                }

                @Override
                public synchronized void update(Graphics g) {
                    cs.updateCircuit(g);
                }

                @Override
                public void paint(Graphics g) {
                    cs.updateCircuit(g);
                }
            };
        }
    }

    public void init() {
        if (!runOnCanvas) {
            final BufferedImage bi = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
            cs.handleResize();
            cs.needAnalyze();
            cs.updateCircuit(bi.getGraphics());
//            cs.analyzeCircuit();
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(sleep);
                            cs.updateCircuit(bi.getGraphics());
                        } catch (Exception ex) {
                            System.out.println(this);
                            ex.printStackTrace();
                        }
                    }
                }
            }.start();

            if (debugFrame) {
                JFrame f = new JFrame();
                f.getContentPane().add(new JPanel() {
                    {
                        new Thread() {
                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException ex) {
                                    }
                                    repaint();
                                }
                            }
                        }.start();
                    }

                    @Override
                    public void paintComponent(Graphics g) {
                        g.drawImage(bi, 0, 0, null);
                    }
                });
                f.setSize(new Dimension(300, 300));
                f.setVisible(true);
            }
        }
    }

    public void repaintCanvas() {
        if (runOnCanvas) {
            canvas.repaint();
        } else {
            up = true;
        }
    }

    public void repaintCanvas(long l) {
        if (runOnCanvas) {
            canvas.repaint(l);
        } else {
            up = true;
            sleep = l;
        }
    }

    public void setCanvasCursor(Cursor cursor) {
        if (runOnCanvas) {
            canvas.setCursor(cursor);
        }
    }

    public Dimension getCanvasSize() {
        if (runOnCanvas) {
            return canvas.getSize();
        } else {
            return new Dimension(300, 400);
        }
    }

    public Component getCanvas() {
        if (runOnCanvas) {
            return canvas;
        } else {
            if (canvas == null) {
                canvas = new Canvas();
            }
            return canvas;
        }
    }

    public void addListeners(CircuitController gui) {
        if (runOnCanvas) {
            canvas.addComponentListener(gui);
            canvas.addMouseMotionListener(gui);
            canvas.addMouseListener(gui);
            canvas.addKeyListener(gui);
        }
    }

    public void setBackground(Color color) {
        if (runOnCanvas) {
            canvas.setBackground(color);
        }
    }

    public void setForeground(Color color) {
        if (runOnCanvas) {
            canvas.setForeground(color);
        }
    }

    public Image createImage(MemoryImageSource imageSource) {
        if (runOnCanvas) {
            return canvas.createImage(imageSource);
        } else {
            return null;
        }
    }
};
