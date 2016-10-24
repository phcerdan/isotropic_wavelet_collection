/*
 * Decompiled with CFR 0_118.
 */
package additionaluserinterface;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class SpinnerDouble
extends JSpinner {
    private SpinnerNumberModel model;
    private double defValue;
    private double minValue;
    private double maxValue;
    private double incValue;

    public SpinnerDouble(double d, double d2, double d3, double d4) {
        this.defValue = d;
        this.minValue = d2;
        this.maxValue = d3;
        this.incValue = d4;
        Double d5 = new Double(d);
        Double d6 = new Double(d2);
        Double d7 = new Double(d3);
        Double d8 = new Double(d4);
        this.model = new SpinnerNumberModel(d5, d6, d7, d8);
        this.setModel(this.model);
    }

    public void setLimit(double d, double d2) {
        this.minValue = d;
        this.maxValue = d2;
        double d3 = this.get();
        Double d4 = new Double(d);
        Double d5 = new Double(d2);
        Double d6 = new Double(this.incValue);
        this.defValue = d3 > d2 ? d2 : (d3 < d ? d : d3);
        Double d7 = new Double(this.defValue);
        this.model = new SpinnerNumberModel(d7, d4, d5, d6);
        this.setModel(this.model);
    }

    public void setIncrement(double d) {
        this.incValue = d;
        Double d2 = (Double)this.getModel().getValue();
        Double d3 = new Double(this.minValue);
        Double d4 = new Double(this.maxValue);
        Double d5 = new Double(d);
        this.model = new SpinnerNumberModel(d2, d3, d4, d5);
        this.setModel(this.model);
    }

    public double getIncrement() {
        return this.incValue;
    }

    public void set(double d) {
        d = d > this.maxValue ? this.maxValue : (d < this.minValue ? this.minValue : d);
        this.model.setValue(d);
    }

    public double get() {
        if (this.model.getValue() instanceof Integer) {
            Integer n = (Integer)this.model.getValue();
            return n.intValue();
        }
        if (this.model.getValue() instanceof Double) {
            Double d = (Double)this.model.getValue();
            return d;
        }
        if (this.model.getValue() instanceof Float) {
            Float f = (Float)this.model.getValue();
            return f.floatValue();
        }
        return 0.0;
    }
}

