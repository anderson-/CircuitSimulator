package com.falstad.circuit;

import com.falstad.circuit.EditDialog.Editable;

class EditOptions implements Editable {

    CircuitSimulator sim;

    public EditOptions(CircuitSimulator s) {
        sim = s;
    }

    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            return new EditInfo("Time step size (s)", sim.getTimeStep(), 0, 0);
        }
        if (n == 1) {
            return new EditInfo("Range for voltage color (V)", sim.getVoltageRange(), 0, 0);
        }
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
        if (n == 0 && ei.value > 0) {
            sim.setTimeStep(ei.value);
        }
        if (n == 1 && ei.value > 0) {
            sim.setVoltageRange(ei.value);
        }
    }
};
