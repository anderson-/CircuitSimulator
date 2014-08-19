/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.falstad.circuit;

import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import static java.awt.geom.Point2D.distanceSq;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;

/**
 *
 * @author antunes
 */
public class CircuitController implements ComponentListener, ActionListener, AdjustmentListener,
        MouseMotionListener, MouseListener, ItemListener, KeyListener {

    static final int MODE_ADD_ELM = 0;
    static final int MODE_DRAG_ALL = 1;
    static final int MODE_DRAG_ROW = 2;
    static final int MODE_DRAG_COLUMN = 3;
    static final int MODE_DRAG_SELECTED = 4;
    static final int MODE_DRAG_POST = 5;
    static final int MODE_SELECT = 6;
    JLabel titleLabel;
    JButton resetButton;
    JButton dumpMatrixButton;
    JMenuItem exportItem, exportLinkItem, importItem, undoItem, redoItem,
            cutItem, copyItem, pasteItem, selectAllItem, optionsItem;
    JMenu optionsMenu;
    public JCheckBox stoppedCheck;
    public JCheckBoxMenuItem dotsCheckItem;
    public JCheckBoxMenuItem voltsCheckItem;
    public JCheckBoxMenuItem powerCheckItem;
    public JCheckBoxMenuItem smallGridCheckItem;
    public JCheckBoxMenuItem showValuesCheckItem;
    public JCheckBoxMenuItem conductanceCheckItem;
    public JCheckBoxMenuItem euroResistorCheckItem;
    public JCheckBoxMenuItem printableCheckItem;
    public JCheckBoxMenuItem conventionCheckItem;
    JScrollBar speedBar;
    JScrollBar currentBar;
    JLabel powerLabel;
    JScrollBar powerBar;
    JPopupMenu elmMenu;
    JMenuItem elmEditMenuItem;
    JMenuItem elmCutMenuItem;
    JMenuItem elmCopyMenuItem;
    JMenuItem elmDeleteMenuItem;
    JPopupMenu mainMenu;
    CircuitSimulator sim;

    public CircuitController(CircuitSimulator sim) {
        this.sim = sim;
    }

    public void requestFocus() {
//        super.requestFocus();
//        sim.cv.requestFocus();//TODO
    }

    public JMenuItem getMenuItem(String s) {
        JMenuItem mi = new JMenuItem(s);
        mi.addActionListener(this);
        return mi;
    }

    public JMenuItem getMenuItem(String s, String ac) {
        JMenuItem mi = new JMenuItem(s);
        mi.setActionCommand(ac);
        mi.addActionListener(this);
        return mi;
    }

    public JCheckBoxMenuItem getCheckItem(String s) {
        JCheckBoxMenuItem mi = new JCheckBoxMenuItem(s);
        mi.addItemListener(this);
        mi.setActionCommand("");
        return mi;
    }

    public JCheckBoxMenuItem getClassCheckItem(String s, String t) {
        try {
            Class c = Class.forName("com.falstad.circuit.elements." + t);
            CircuitElm elm = sim.constructElement(c, 0, 0);
            sim.register(c, elm);
            if (elm.needsShortcut()) {
                s += " (" + (char) elm.getShortcut() + ")";
            }
            elm.delete();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return getCheckItem(s, t);
    }

    public JCheckBoxMenuItem getCheckItem(String s, String t) {
        JCheckBoxMenuItem mi = new JCheckBoxMenuItem(s);
        mi.addItemListener(this);
        mi.setActionCommand(t);
        return mi;
    }

    public void paint(Graphics g) {
        sim.cv.repaintCanvas();
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
        sim.cv.repaintCanvas();
    }

    public void componentResized(ComponentEvent e) {
        sim.handleResize();
        sim.cv.repaintCanvas(100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        if (e.getSource() == resetButton) {
            sim.setStopped(!sim.isStopped());
            int i;

            // on IE, drawImage() stops working inexplicably every once in
            // a while.  Recreating it fixes the problem, so we do that here.
            sim.dbimage = sim.getContainer().createImage(sim.winSize.width, sim.winSize.height);

            for (i = 0; i != sim.elmListSize(); i++) {
                sim.getElm(i).reset();
            }
            for (i = 0; i != sim.scopeCount; i++) {
                sim.scopes[i].resetGraph();
            }
            sim.analyzeFlag = true;
            sim.t = 0;
            stoppedCheck.setSelected(false);
            sim.cv.repaintCanvas();
        }
        if (e.getSource() == dumpMatrixButton) {
            sim.dumpMatrix = true;
        }
        if (e.getSource() == exportItem) {
            sim.doExport(false);
        }
        if (e.getSource() == optionsItem) {
            sim.doEdit(new EditOptions(sim));
        }
        if (e.getSource() == importItem) {
            sim.doImport();
        }
        if (e.getSource() == exportLinkItem) {
            sim.doExport(true);
        }
        if (e.getSource() == undoItem) {
            sim.doUndo();
        }
        if (e.getSource() == redoItem) {
            sim.doRedo();
        }
        if (ac.compareTo("Cut") == 0) {
            if (e.getSource() != elmCutMenuItem) {
                sim.menuElm = null;
            }
            sim.doCut();
        }
        if (ac.compareTo("Copy") == 0) {
            if (e.getSource() != elmCopyMenuItem) {
                sim.menuElm = null;
            }
            sim.doCopy();
        }
        if (ac.compareTo("Paste") == 0) {
            sim.doPaste();
        }
        if (e.getSource() == selectAllItem) {
            sim.doSelectAll();
        }
        if (ac.compareTo("stackAll") == 0) {
            sim.stackAll();
        }
        if (ac.compareTo("unstackAll") == 0) {
            sim.unstackAll();
        }
        if (e.getSource() == elmEditMenuItem) {
            sim.doEdit(sim.menuElm);
        }
        if (ac.compareTo("Delete") == 0) {
            if (e.getSource() != elmDeleteMenuItem) {
                sim.menuElm = null;
            }
            sim.doDelete();
        }
        if (e.getSource() == Scope.elmScopeMenuItem && sim.menuElm != null) {
            int i;
            for (i = 0; i != sim.scopeCount; i++) {
                if (sim.scopes[i].getElm() == null) {
                    break;
                }
            }
            if (i == sim.scopeCount) {
                if (sim.scopeCount == sim.scopes.length) {
                    return;
                }
                sim.scopeCount++;
                sim.scopes[i] = new Scope(sim);
                sim.scopes[i].setPosition(i);
                sim.handleResize();
            }
            sim.scopes[i].setElm(sim.menuElm);
        }
        if (sim.menuScope != -1) {
            if (ac.compareTo("remove") == 0) {
                sim.scopes[sim.menuScope].setElm(null);
            }
            if (ac.compareTo("speed2") == 0) {
                sim.scopes[sim.menuScope].speedUp();
            }
            if (ac.compareTo("speed1/2") == 0) {
                sim.scopes[sim.menuScope].slowDown();
            }
            if (ac.compareTo("scale") == 0) {
                sim.scopes[sim.menuScope].adjustScale(.5);
            }
            if (ac.compareTo("maxscale") == 0) {
                sim.scopes[sim.menuScope].adjustScale(1e-50);
            }
            if (ac.compareTo("stack") == 0) {
                sim.stackScope(sim.menuScope);
            }
            if (ac.compareTo("unstack") == 0) {
                sim.unstackScope(sim.menuScope);
            }
            if (ac.compareTo("selecty") == 0) {
                sim.scopes[sim.menuScope].selectY();
            }
            if (ac.compareTo("reset") == 0) {
                sim.scopes[sim.menuScope].resetGraph();
            }
            sim.cv.repaintCanvas();
        }
        if (ac.indexOf("setup ") == 0) {
            sim.pushUndo();
            sim.readSetupFile(ac.substring(6), ((JMenuItem) e.getSource()).getLabel());
        }
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        System.out.print(((JScrollBar) e.getSource()).getValue() + "\n");
    }

    public void mouseDragged(MouseEvent e) {
        // ignore right mouse button with no modifiers (needed on PC)
        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
            int ex = e.getModifiersEx();
            if ((ex & (MouseEvent.META_DOWN_MASK
                    | MouseEvent.SHIFT_DOWN_MASK
                    | MouseEvent.CTRL_DOWN_MASK
                    | MouseEvent.ALT_DOWN_MASK)) == 0) {
                return;
            }
        }
        if (!sim.circuitArea.contains(e.getX(), e.getY())) {
            return;
        }
        if (sim.dragElm != null) {
            sim.dragElm.drag(e.getX(), e.getY());
        }
        boolean success = true;
        switch (sim.tempMouseMode) {
            case MODE_DRAG_ALL:
                sim.dragAll(sim.snapGrid(e.getX()), sim.snapGrid(e.getY()));
                break;
            case MODE_DRAG_ROW:
                sim.dragRow(sim.snapGrid(e.getX()), sim.snapGrid(e.getY()));
                break;
            case MODE_DRAG_COLUMN:
                sim.dragColumn(sim.snapGrid(e.getX()), sim.snapGrid(e.getY()));
                break;
            case MODE_DRAG_POST:
                if (sim.mouseElm != null) {
                    sim.dragPost(sim.snapGrid(e.getX()), sim.snapGrid(e.getY()));
                }
                break;
            case MODE_SELECT:
                if (sim.mouseElm == null) {
                    sim.selectArea(e.getX(), e.getY());
                } else {
                    sim.tempMouseMode = MODE_DRAG_SELECTED;
                    success = sim.dragSelected(e.getX(), e.getY());
                }
                break;
            case MODE_DRAG_SELECTED:
                success = sim.dragSelected(e.getX(), e.getY());
                break;
        }
        sim.dragging = true;
        if (success) {
            if (sim.tempMouseMode == MODE_DRAG_SELECTED && sim.mouseElm instanceof GraphicElm) {
                sim.dragX = e.getX();
                sim.dragY = e.getY();
            } else {
                sim.dragX = sim.snapGrid(e.getX());
                sim.dragY = sim.snapGrid(e.getY());
            }
        }
        sim.cv.repaintCanvas(sim.pause);
    }

    public void mouseMoved(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            return;
        }
        int x = e.getX();
        int y = e.getY();
        sim.dragX = sim.snapGrid(x);
        sim.dragY = sim.snapGrid(y);
        sim.draggingPost = -1;
        int i;
        CircuitElm origMouse = sim.mouseElm;
        sim.mouseElm = null;
        sim.mousePost = -1;
        sim.plotXElm = sim.plotYElm = null;
        int bestDist = 100000;
        int bestArea = 100000;
        for (i = 0; i != sim.elmListSize(); i++) {
            CircuitElm ce = sim.getElm(i);
            if (ce.boundingBox.contains(x, y)) {
                int j;
                int area = ce.boundingBox.width * ce.boundingBox.height;
                int jn = ce.getPostCount();
                if (jn > 2) {
                    jn = 2;
                }
                for (j = 0; j != jn; j++) {
                    Point pt = ce.getPost(j);
                    int dist = sim.distanceSq(x, y, pt.x, pt.y);

                    // if multiple elements have overlapping bounding boxes,
                    // we prefer selecting elements that have posts close
                    // to the mouse pointer and that have a small bounding
                    // box area.
                    if (dist <= bestDist && area <= bestArea) {
                        bestDist = dist;
                        bestArea = area;
                        sim.mouseElm = ce;
                    }
                }
                if (ce.getPostCount() == 0) {
                    sim.mouseElm = ce;
                }
            }
        }
        sim.scopeSelected = -1;
        if (sim.mouseElm == null) {
            for (i = 0; i != sim.scopeCount; i++) {
                Scope s = sim.scopes[i];
                if (s.getRect().contains(x, y)) {
                    s.select();
                    sim.scopeSelected = i;
                }
            }
            // the mouse pointer was not in any of the bounding boxes, but we
            // might still be close to a post
            for (i = 0; i != sim.elmListSize(); i++) {
                CircuitElm ce = sim.getElm(i);
                int j;
                int jn = ce.getPostCount();
                for (j = 0; j != jn; j++) {
                    Point pt = ce.getPost(j);
                    int dist = sim.distanceSq(x, y, pt.x, pt.y);
                    if (distanceSq(pt.x, pt.y, x, y) < 26) {
                        sim.mouseElm = ce;
                        sim.mousePost = j;
                        break;
                    }
                }
            }
        } else {
            sim.mousePost = -1;
            // look for post close to the mouse pointer
            for (i = 0; i != sim.mouseElm.getPostCount(); i++) {
                Point pt = sim.mouseElm.getPost(i);
                if (distanceSq(pt.x, pt.y, x, y) < 26) {
                    sim.mousePost = i;
                }
            }
        }
        if (sim.mouseElm != origMouse) {
            sim.cv.repaintCanvas();
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && !sim.didSwitch) {
            doEditMenu(e);
        }
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            if (sim.mouseMode == MODE_SELECT || sim.mouseMode == MODE_DRAG_SELECTED) {
                sim.clearSelection();
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        sim.scopeSelected = -1;
        sim.mouseElm = sim.plotXElm = sim.plotYElm = null;
        sim.cv.repaintCanvas();
    }

    public void mousePressed(MouseEvent e) {
        sim.didSwitch = false;

        System.out.println("mod:" + e.getModifiers());
        int ex = e.getModifiersEx();
        if ((ex & (MouseEvent.META_DOWN_MASK
                | MouseEvent.SHIFT_DOWN_MASK)) == 0 && e.isPopupTrigger()) {
            doPopupMenu(e);
            return;
        }
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            // left mouse
            sim.tempMouseMode = sim.mouseMode;
            if ((ex & MouseEvent.ALT_DOWN_MASK) != 0
                    && (ex & MouseEvent.META_DOWN_MASK) != 0) {
                sim.tempMouseMode = MODE_DRAG_COLUMN;
            } else if ((ex & MouseEvent.ALT_DOWN_MASK) != 0
                    && (ex & MouseEvent.SHIFT_DOWN_MASK) != 0) {
                sim.tempMouseMode = MODE_DRAG_ROW;
            } else if ((ex & MouseEvent.SHIFT_DOWN_MASK) != 0) {
                sim.tempMouseMode = MODE_SELECT;
            } else if ((ex & MouseEvent.ALT_DOWN_MASK) != 0) {
                sim.tempMouseMode = MODE_DRAG_ALL;
            } else if ((ex & (MouseEvent.CTRL_DOWN_MASK
                    | MouseEvent.META_DOWN_MASK)) != 0) {
                sim.tempMouseMode = MODE_DRAG_POST;
            }
        } else if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
            // right mouse
            if ((ex & MouseEvent.SHIFT_DOWN_MASK) != 0) {
                sim.tempMouseMode = MODE_DRAG_ROW;
            } else if ((ex & (MouseEvent.CTRL_DOWN_MASK
                    | MouseEvent.META_DOWN_MASK)) != 0) {
                sim.tempMouseMode = MODE_DRAG_COLUMN;
            } else {
                return;
            }
        }

        if (sim.tempMouseMode != MODE_SELECT && sim.tempMouseMode != MODE_DRAG_SELECTED) {
            sim.clearSelection();
        }
        if (sim.doSwitch(e.getX(), e.getY())) {
            sim.didSwitch = true;
            return;
        }

        sim.pushUndo();
        sim.initDragX = e.getX();
        sim.initDragY = e.getY();
        sim.dragging = true;
        if (sim.tempMouseMode != MODE_ADD_ELM || sim.addingClass == null) {
            return;
        }

        int x0 = sim.snapGrid(e.getX());
        int y0 = sim.snapGrid(e.getY());
        if (!sim.circuitArea.contains(x0, y0)) {
            return;
        }

        sim.dragElm = sim.constructElement(sim.addingClass, x0, y0);
    }

    // hausen: add doEditMenu
    void doEditMenu(MouseEvent e) {
        if (sim.mouseElm != null) {
            sim.doEdit(sim.mouseElm);
        }
    }

    void doPopupMenu(MouseEvent e) {
        sim.menuElm = sim.mouseElm;
        sim.menuScope = -1;
        if (sim.scopeSelected != -1) {
            JPopupMenu m = sim.scopes[sim.scopeSelected].getMenu();
            sim.menuScope = sim.scopeSelected;
            if (m != null) {
                m.show(e.getComponent(), e.getX(), e.getY());
            }
        } else if (sim.mouseElm != null) {
            elmEditMenuItem.setEnabled(sim.mouseElm.getEditInfo(0) != null);
            Scope.elmScopeMenuItem.setEnabled(sim.mouseElm.canViewInScope());
            elmMenu.show(e.getComponent(), e.getX(), e.getY());
        } else {
//            doMainMenuChecks(mainMenu); //TODO
            mainMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    void doMainMenuChecks(JMenu m) {
        int i;
        if (m == optionsMenu) {
            return;
        }
        for (i = 0; i != m.getItemCount(); i++) {
            JMenuItem mc = m.getItem(i);
            if (mc instanceof JMenu) {
                doMainMenuChecks((JMenu) mc);
            }
            if (mc instanceof JCheckBoxMenuItem) {
                JCheckBoxMenuItem cmi = (JCheckBoxMenuItem) mc;
                cmi.setState(sim.mouseModeStr.compareTo(cmi.getActionCommand()) == 0);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        int ex = e.getModifiersEx();
        if ((ex & (MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK
                | MouseEvent.META_DOWN_MASK)) == 0 && e.isPopupTrigger()) {
            doPopupMenu(e);
            return;
        }
        sim.tempMouseMode = sim.mouseMode;
        sim.selectedArea = null;
        sim.dragging = false;
        boolean circuitChanged = false;
        if (sim.heldSwitchElm != null) {
            sim.heldSwitchElm.mouseUp();
            sim.heldSwitchElm = null;
            circuitChanged = true;
        }
        if (sim.dragElm != null) {
            // if the element is zero size then don't create it
            if (sim.dragElm.x == sim.dragElm.x2 && sim.dragElm.y == sim.dragElm.y2) {
                sim.dragElm.delete();
            } else {
                sim.addElement(sim.dragElm);
                circuitChanged = true;
            }
            sim.dragElm = null;
        }
        if (circuitChanged) {
            sim.needAnalyze();
        }
        if (sim.dragElm != null) {
            sim.dragElm.delete();
        }
        sim.dragElm = null;
        sim.cv.repaintCanvas();
    }

    public void itemStateChanged(ItemEvent e) {
        sim.cv.repaintCanvas(sim.pause);
        Object mi = e.getItemSelectable();
        if (mi == stoppedCheck) {
            return;
        }
        if (mi == smallGridCheckItem) {
            sim.setGrid();
        }
        if (mi == powerCheckItem) {
            if (powerCheckItem.getState()) {
                voltsCheckItem.setState(false);
            } else {
                voltsCheckItem.setState(true);
            }
        }
        if (mi == voltsCheckItem && voltsCheckItem.getState() && powerCheckItem != null) {
            powerCheckItem.setState(false);
        }
        sim.enableItems();
        if (sim.menuScope != -1) {
            Scope sc = sim.scopes[sim.menuScope];
            sc.handleMenu(e, mi);
        }
        if (mi instanceof JCheckBoxMenuItem) {
            JMenuItem mmi = (JMenuItem) mi;
            int prevMouseMode = sim.mouseMode;
            sim.setMouseMode(MODE_ADD_ELM);
            String s = mmi.getActionCommand();
            if (s.length() > 0) {
                sim.mouseModeStr = s;
            }
            if (s.compareTo("DragAll") == 0) {
                sim.setMouseMode(MODE_DRAG_ALL);
            } else if (s.compareTo("DragRow") == 0) {
                sim.setMouseMode(MODE_DRAG_ROW);
            } else if (s.compareTo("DragColumn") == 0) {
                sim.setMouseMode(MODE_DRAG_COLUMN);
            } else if (s.compareTo("DragSelected") == 0) {
                sim.setMouseMode(MODE_DRAG_SELECTED);
            } else if (s.compareTo("DragPost") == 0) {
                sim.setMouseMode(MODE_DRAG_POST);
            } else if (s.compareTo("Select") == 0) {
                sim.setMouseMode(MODE_SELECT);
            } else if (s.length() > 0) {
                try {
                    sim.addingClass = Class.forName("com.falstad.circuit.elements." + s);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            } else {
                sim.setMouseMode(prevMouseMode);
            }
            sim.tempMouseMode = sim.mouseMode;
        }
    }

    public JMenuBar createGUI(boolean useFrame) {
        useFrame = true;//tmp
        mainMenu = new JPopupMenu();
        JMenuBar mb = null;
        if (useFrame) {
            mb = new JMenuBar();
        }
        JMenu m = new JMenu("File");
        if (useFrame) {
            mb.add(m);
        } else {
            mainMenu.add(m);
        }
        m.add(importItem = getMenuItem("Import"));
        m.add(exportItem = getMenuItem("Export"));
        m.add(exportLinkItem = getMenuItem("Export Link"));
        m.addSeparator();

        m = new JMenu("Edit");
        m.add(undoItem = getMenuItem("Undo"));
        undoItem.setAccelerator(KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_DOWN_MASK));
        m.add(redoItem = getMenuItem("Redo"));
        redoItem.setAccelerator(KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        m.addSeparator();
        m.add(cutItem = getMenuItem("Cut"));
        cutItem.setAccelerator(KeyStroke.getKeyStroke('X', KeyEvent.CTRL_DOWN_MASK));
        m.add(copyItem = getMenuItem("Copy"));
        copyItem.setAccelerator(KeyStroke.getKeyStroke('C', KeyEvent.CTRL_DOWN_MASK));
        m.add(pasteItem = getMenuItem("Paste"));
        pasteItem.setAccelerator(KeyStroke.getKeyStroke('V', KeyEvent.CTRL_DOWN_MASK));
//        pasteItem.setEnabled(false);
        m.add(selectAllItem = getMenuItem("Select All"));
        selectAllItem.setAccelerator(KeyStroke.getKeyStroke('A', KeyEvent.CTRL_DOWN_MASK));
        if (useFrame) {
            mb.add(m);
        } else {
            mainMenu.add(m);
        }

        m = new JMenu("Scope");
        if (useFrame) {
            mb.add(m);
        } else {
            mainMenu.add(m);
        }
        m.add(getMenuItem("Stack All", "stackAll"));
        m.add(getMenuItem("Unstack All", "unstackAll"));

        optionsMenu = m = new JMenu("Options");
        if (useFrame) {
            mb.add(m);
        } else {
            mainMenu.add(m);
        }
        m.add(dotsCheckItem = getCheckItem("Show Current"));
        dotsCheckItem.setState(true);
        m.add(voltsCheckItem = getCheckItem("Show Voltage"));
        voltsCheckItem.setState(true);
        m.add(powerCheckItem = getCheckItem("Show Power"));
        m.add(showValuesCheckItem = getCheckItem("Show Values"));
        showValuesCheckItem.setState(true);
        //m.add(conductanceCheckItem = getCheckItem("Show Conductance"));
        m.add(smallGridCheckItem = getCheckItem("Small Grid"));
        m.add(euroResistorCheckItem = getCheckItem("European Resistors"));
        euroResistorCheckItem.setState(sim.euroResistor());
        m.add(printableCheckItem = getCheckItem("White Background"));
        printableCheckItem.setState(sim.whiteBackground());
        m.add(conventionCheckItem = getCheckItem("Conventional Current Motion"));
//        conventionCheckItem.setState(convention); //TODO
        m.add(optionsItem = getMenuItem("Other Options..."));

        mainMenu.add(getClassCheckItem("Add Wire", "WireElm"));
        mainMenu.add(getClassCheckItem("Add Resistor", "ResistorElm"));

        JMenu passMenu = new JMenu("Passive Components");
        mainMenu.add(passMenu);
        passMenu.add(getClassCheckItem("Add Capacitor", "CapacitorElm"));
        passMenu.add(getClassCheckItem("Add Inductor", "InductorElm"));
        passMenu.add(getClassCheckItem("Add Switch", "SwitchElm"));
        passMenu.add(getClassCheckItem("Add Push Switch", "PushSwitchElm"));
        passMenu.add(getClassCheckItem("Add SPDT Switch", "Switch2Elm"));
        passMenu.add(getClassCheckItem("Add Potentiometer", "PotElm"));
        passMenu.add(getClassCheckItem("Add Transformer", "TransformerElm"));
        passMenu.add(getClassCheckItem("Add Tapped Transformer",
                "TappedTransformerElm"));
        passMenu.add(getClassCheckItem("Add Transmission Line", "TransLineElm"));
        passMenu.add(getClassCheckItem("Add Relay", "RelayElm"));
        passMenu.add(getClassCheckItem("Add Memristor", "MemristorElm"));
        passMenu.add(getClassCheckItem("Add Spark Gap", "SparkGapElm"));

        JMenu inputMenu = new JMenu("Inputs/Outputs");
        mainMenu.add(inputMenu);
        inputMenu.add(getClassCheckItem("Add Ground", "GroundElm"));
        inputMenu.add(getClassCheckItem("Add Voltage Source (2-terminal)", "DCVoltageElm"));
        inputMenu.add(getClassCheckItem("Add A/C Source (2-terminal)", "ACVoltageElm"));
        inputMenu.add(getClassCheckItem("Add Voltage Source (1-terminal)", "RailElm"));
        inputMenu.add(getClassCheckItem("Add A/C Source (1-terminal)", "ACRailElm"));
        inputMenu.add(getClassCheckItem("Add Square Wave (1-terminal)", "SquareRailElm"));
        inputMenu.add(getClassCheckItem("Add Analog Output", "OutputElm"));
        inputMenu.add(getClassCheckItem("Add Logic Input", "LogicInputElm"));
        inputMenu.add(getClassCheckItem("Add Logic Output", "LogicOutputElm"));
        inputMenu.add(getClassCheckItem("Add Clock", "ClockElm"));
        inputMenu.add(getClassCheckItem("Add A/C Sweep", "SweepElm"));
        inputMenu.add(getClassCheckItem("Add Var. Voltage", "VarRailElm"));
        inputMenu.add(getClassCheckItem("Add Antenna", "AntennaElm"));
        inputMenu.add(getClassCheckItem("Add AM source", "AMElm"));
        inputMenu.add(getClassCheckItem("Add FM source", "FMElm"));
        inputMenu.add(getClassCheckItem("Add Current Source", "CurrentElm"));
        inputMenu.add(getClassCheckItem("Add LED", "LEDElm"));
        inputMenu.add(getClassCheckItem("Add Lamp (beta)", "LampElm"));
        inputMenu.add(getClassCheckItem("Add LED Matrix", "LEDMatrixElm"));
        //inputMenu.add(getClassCheckItem("Add Microphone Input", "SignalInElm"));
        //inputMenu.add(getClassCheckItem("Add Speaker Output", "SignalOutElm"));

        JMenu activeMenu = new JMenu("Active Components");
        mainMenu.add(activeMenu);
        activeMenu.add(getClassCheckItem("Add Diode", "DiodeElm"));
        activeMenu.add(getClassCheckItem("Add Zener Diode", "ZenerElm"));
        activeMenu.add(getClassCheckItem("Add Transistor (bipolar, NPN)",
                "NTransistorElm"));
        activeMenu.add(getClassCheckItem("Add Transistor (bipolar, PNP)",
                "PTransistorElm"));
        activeMenu.add(getClassCheckItem("Add Op Amp (- on top)", "OpAmpElm"));
        activeMenu.add(getClassCheckItem("Add Op Amp (+ on top)",
                "OpAmpSwapElm"));
        activeMenu.add(getClassCheckItem("Add MOSFET (n-channel)",
                "NMosfetElm"));
        activeMenu.add(getClassCheckItem("Add MOSFET (p-channel)",
                "PMosfetElm"));
        activeMenu.add(getClassCheckItem("Add JFET (n-channel)",
                "NJfetElm"));
        activeMenu.add(getClassCheckItem("Add JFET (p-channel)",
                "PJfetElm"));
        activeMenu.add(getClassCheckItem("Add Analog Switch (SPST)", "AnalogSwitchElm"));
        activeMenu.add(getClassCheckItem("Add Analog Switch (SPDT)", "AnalogSwitch2Elm"));
        activeMenu.add(getClassCheckItem("Add Tristate buffer", "TriStateElm"));
        activeMenu.add(getClassCheckItem("Add Schmitt Trigger", "SchmittElm"));
        activeMenu.add(getClassCheckItem("Add Schmitt Trigger (Inverting)", "InvertingSchmittElm"));
        activeMenu.add(getClassCheckItem("Add SCR", "SCRElm"));
        //activeMenu.add(getClassCheckItem("Add Varactor/Varicap", "VaractorElm"));
        activeMenu.add(getClassCheckItem("Add Tunnel Diode", "TunnelDiodeElm"));
        activeMenu.add(getClassCheckItem("Add Triode", "TriodeElm"));
        //activeMenu.add(getClassCheckItem("Add Diac", "DiacElm"));
        //activeMenu.add(getClassCheckItem("Add Triac", "TriacElm"));
//        activeMenu.add(getClassCheckItem("Add Photoresistor", "PhotoResistorElm"));
        //activeMenu.add(getClassCheckItem("Add Thermistor", "ThermistorElm"));
        activeMenu.add(getClassCheckItem("Add CCII+", "CC2Elm"));
        activeMenu.add(getClassCheckItem("Add CCII-", "CC2NegElm"));

        JMenu gateMenu = new JMenu("Logic Gates");
        mainMenu.add(gateMenu);
        gateMenu.add(getClassCheckItem("Add Inverter", "InverterElm"));
        gateMenu.add(getClassCheckItem("Add NAND Gate", "NandGateElm"));
        gateMenu.add(getClassCheckItem("Add NOR Gate", "NorGateElm"));
        gateMenu.add(getClassCheckItem("Add AND Gate", "AndGateElm"));
        gateMenu.add(getClassCheckItem("Add OR Gate", "OrGateElm"));
        gateMenu.add(getClassCheckItem("Add XOR Gate", "XorGateElm"));

        JMenu chipMenu = new JMenu("Chips");
        mainMenu.add(chipMenu);
        chipMenu.add(getClassCheckItem("Add D Flip-Flop", "DFlipFlopElm"));
        chipMenu.add(getClassCheckItem("Add JK Flip-Flop", "JKFlipFlopElm"));
        chipMenu.add(getClassCheckItem("Add T Flip-Flop", "TFlipFlopElm"));
        chipMenu.add(getClassCheckItem("Add 7 Segment LED", "SevenSegElm"));
        chipMenu.add(getClassCheckItem("Add 7 Segment Decoder", "SevenSegDecoderElm"));
        chipMenu.add(getClassCheckItem("Add Multiplexer", "MultiplexerElm"));
        chipMenu.add(getClassCheckItem("Add Demultiplexer", "DeMultiplexerElm"));
        chipMenu.add(getClassCheckItem("Add SIPO shift register", "SipoShiftElm"));
        chipMenu.add(getClassCheckItem("Add PISO shift register", "PisoShiftElm"));
        chipMenu.add(getClassCheckItem("Add Phase Comparator", "PhaseCompElm"));
        chipMenu.add(getClassCheckItem("Add Counter", "CounterElm"));
        chipMenu.add(getClassCheckItem("Add Decade Counter", "DecadeElm"));
        chipMenu.add(getClassCheckItem("Add 555 Timer", "TimerElm"));
        chipMenu.add(getClassCheckItem("Add DAC", "DACElm"));
        chipMenu.add(getClassCheckItem("Add ADC", "ADCElm"));
        chipMenu.add(getClassCheckItem("Add Latch", "LatchElm"));
        //chipMenu.add(getClassCheckItem("Add Static RAM", "SRAMElm"));
        chipMenu.add(getClassCheckItem("Add Sequence generator", "SeqGenElm"));
        chipMenu.add(getClassCheckItem("Add VCO", "VCOElm"));
        chipMenu.add(getClassCheckItem("Add Full Adder", "FullAdderElm"));
        chipMenu.add(getClassCheckItem("Add Half Adder", "HalfAdderElm"));

        JMenu otherMenu = new JMenu("Other");
        mainMenu.add(otherMenu);
        otherMenu.add(getClassCheckItem("Add Text", "TextElm"));
        otherMenu.add(getClassCheckItem("Add Box", "BoxElm"));
        otherMenu.add(getClassCheckItem("Add Scope Probe", "ProbeElm"));
        otherMenu.add(getCheckItem("Drag All (Alt-drag)", "DragAll"));
        otherMenu.add(getCheckItem(
                sim.isMac ? "Drag Row (Alt-S-drag, S-right)"
                : "Drag Row (S-right)",
                "DragRow"));
        otherMenu.add(getCheckItem(
                sim.isMac ? "Drag Column (Alt-\u2318-drag, \u2318-right)"
                : "Drag Column (C-right)",
                "DragColumn"));
        otherMenu.add(getCheckItem("Drag Selected", "DragSelected"));
        otherMenu.add(getCheckItem("Drag Post (" + sim.ctrlMetaKey + "-drag)",
                "DragPost"));

        mainMenu.add(getCheckItem("Select/Drag Selected (space or Shift-drag)", "Select"));
        sim.getContainer().add(mainMenu);

        createSideBar(mb, useFrame, sim.getContainer());

//        if (useFrame) {
//            setMenuBar(mb);
//        }
        return mb;
    }

    public void createSideBar(JMenuBar mb, boolean useFrame, Container c) {

        JMenu circuitsMenu = new JMenu("Circuits");
        if (useFrame) {
            mb.add(circuitsMenu);
        } else {
            mainMenu.add(circuitsMenu);
        }

        c.add(resetButton = new JButton("Reset"));
        resetButton.addActionListener(this);
        dumpMatrixButton = new JButton("Dump Matrix");
        //c.add(dumpMatrixButton);
        dumpMatrixButton.addActionListener(this);
        stoppedCheck = new JCheckBox("Stopped");
        stoppedCheck.addItemListener(this);
        c.add(stoppedCheck);

        c.add(new Label("Simulation Speed", JLabel.CENTER));

        // was max of 140
        c.add(speedBar = new JScrollBar(JScrollBar.HORIZONTAL, 3, 1, 0, 260));
        speedBar.addAdjustmentListener(this);

        c.add(new Label("Current Speed", JLabel.CENTER));
        currentBar = new JScrollBar(JScrollBar.HORIZONTAL,
                50, 1, 1, 100);
        currentBar.addAdjustmentListener(this);
        c.add(currentBar);

        c.add(powerLabel = new JLabel("Power Brightness", JLabel.CENTER));
        c.add(powerBar = new JScrollBar(JScrollBar.HORIZONTAL,
                50, 1, 1, 100));
        powerBar.addAdjustmentListener(this);
        powerBar.disable();
        powerLabel.disable();

        c.add(new Label("www.falstad.com"));

        if (useFrame) {
            c.add(new Label(""));
        }
        Font f = new Font("SansSerif", 0, 10);
        Label l;
        l = new Label("Current Circuit:");
        l.setFont(f);
        titleLabel = new JLabel("Label");
        titleLabel.setFont(f);
        if (useFrame) {
            c.add(l);
            c.add(titleLabel);
        }

        elmMenu = new JPopupMenu();
        elmMenu.add(elmEditMenuItem = getMenuItem("Edit"));
        elmMenu.add(Scope.elmScopeMenuItem = getMenuItem("View in Scope"));
        elmMenu.add(elmCutMenuItem = getMenuItem("Cut"));
        elmMenu.add(elmCopyMenuItem = getMenuItem("Copy"));
        elmMenu.add(elmDeleteMenuItem = getMenuItem("Delete"));
        c.add(elmMenu);

        Scope.scopeMenu = Scope.buildScopeMenu(false, sim);
        Scope.transScopeMenu = Scope.buildScopeMenu(true, sim);

        sim.getSetupList(circuitsMenu, false);
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 127) {
            sim.doDelete();
            return;
        }
        if (e.getKeyChar() > ' ' && e.getKeyChar() < 127) {
            Class c = sim.shortcuts[e.getKeyChar()];
            if (c == null) {
                return;
            }
            CircuitElm elm = null;
            elm = sim.constructElement(c, 0, 0);
            if (elm == null) {
                return;
            }
            sim.setMouseMode(MODE_ADD_ELM);
            sim.mouseModeStr = c.getName();
            sim.addingClass = c;
        }
        if (e.getKeyChar() == ' ' || e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            sim.setMouseMode(MODE_SELECT);
            sim.mouseModeStr = "Select";
        }
        sim.tempMouseMode = sim.mouseMode;
    }

}
