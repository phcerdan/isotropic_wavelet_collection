/*
 * Decompiled with CFR 0_118.
 */
package additionaluserinterface;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class SpinnerInteger
extends JSpinner {
    private SpinnerNumberModel model;
    private int defValue;
    private int minValue;
    private int maxValue;
    private int incValue;

    public SpinnerInteger(int n, int n2, int n3, int n4) {
        this.defValue = n;
        this.minValue = n2;
        this.maxValue = n3;
        this.incValue = n4;
        Integer n5 = new Integer(n);
        Integer n6 = new Integer(n2);
        Integer n7 = new Integer(n3);
        Integer n8 = new Integer(n4);
        this.model = new SpinnerNumberModel(n5, n6, n7, n8);
        this.setModel(this.model);
    }

    public void setLimit(int n, int n2) {
        this.minValue = n;
        this.maxValue = n2;
        int n3 = this.get();
        Integer n4 = new Integer(n);
        Integer n5 = new Integer(n2);
        Integer n6 = new Integer(this.incValue);
        this.defValue = n3 > n2 ? n2 : (n3 < n ? n : n3);
        Integer n7 = new Integer(this.defValue);
        this.model = new SpinnerNumberModel(n7, n4, n5, n6);
        this.setModel(this.model);
    }

    public void setIncrement(int n) {
        this.incValue = n;
        Integer n2 = (Integer)this.getModel().getValue();
        Integer n3 = new Integer(this.minValue);
        Integer n4 = new Integer(this.maxValue);
        Integer n5 = new Integer(n);
        this.model = new SpinnerNumberModel(n2, n3, n4, n5);
        this.setModel(this.model);
    }

    public int getIncrement() {
        return this.incValue;
    }

    public void set(int n) {
        n = n > this.maxValue ? this.maxValue : (n < this.minValue ? this.minValue : n);
        this.model.setValue(n);
    }

    public int get() {
        if (this.model.getValue() instanceof Integer) {
            Integer n = (Integer)this.model.getValue();
            return n;
        }
        if (this.model.getValue() instanceof Double) {
            Double d = (Double)this.model.getValue();
            return (int)d.doubleValue();
        }
        if (this.model.getValue() instanceof Float) {
            Float f = (Float)this.model.getValue();
            return (int)f.floatValue();
        }
        return 0;
    }
}

