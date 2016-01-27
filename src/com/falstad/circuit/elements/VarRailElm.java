package com.falstad.circuit.elements;

import com.falstad.circuit.CircuitSimulator;
import com.falstad.circuit.EditInfo;
import java.awt.*;
import java.util.StringTokenizer;

public class VarRailElm extends RailElm {

    Scrollbar slider;
    Label label;
    String sliderText;

    public VarRailElm(int xx, int yy) {
        super(xx, yy, WF_VAR);
        sliderText = "Voltage";
        frequency = maxVoltage;
    }

    public VarRailElm(int xa, int ya, int xb, int yb, int f,
            StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
        sliderText = st.nextToken();
        while (st.hasMoreTokens()) {
            sliderText += ' ' + st.nextToken();
        }
    }

    @Override
    public void setSim(CircuitSimulator sim) {
        super.setSim(sim); //To change body of generated methods, choose Tools | Templates.
        createSlider();
    }
    
    public String dump() {
        return super.dump() + " " + sliderText;
    }

    public int getDumpType() {
        return 172;
    }

    void createSlider() {
        waveform = WF_VAR;
        sim.getContainer().add(label = new Label(sliderText, Label.CENTER));
        int value = (int) ((frequency - bias) * 100 / (maxVoltage - bias));
        sim.getContainer().add(slider = new Scrollbar(Scrollbar.HORIZONTAL, value, 1, 0, 101));
        sim.getContainer().validate();
    }

    public double getVoltage() {
        frequency = slider.getValue() * (maxVoltage - bias) / 100. + bias;
        return frequency;
    }

    public void delete() {
        sim.getContainer().remove(label);
        sim.getContainer().remove(slider);
    }

    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            return new EditInfo("Min Voltage", bias, -20, 20);
        }
        if (n == 1) {
            return new EditInfo("Max Voltage", maxVoltage, -20, 20);
        }
        if (n == 2) {
            EditInfo ei = new EditInfo("Slider Text", 0, -1, -1);
            ei.text = sliderText;
            return ei;
        }
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
            bias = ei.value;
        }
        if (n == 1) {
            maxVoltage = ei.value;
        }
        if (n == 2) {
            sliderText = ei.textf.getText();
            label.setText(sliderText);
        }
    }

    public int getShortcut() {
        return 0;
    }
}
