/*
 * Decompiled with CFR 0_118.
 */
package additionaluserinterface;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class SpinnerFloat
extends JSpinner {
    private SpinnerNumberModel model;
    private float defValue;
    private float minValue;
    private float maxValue;
    private float incValue;

    public SpinnerFloat(float f, float f2, float f3, float f4) {
        this.defValue = f;
        this.minValue = f2;
        this.maxValue = f3;
        this.incValue = f4;
        Float f5 = new Float(f);
        Float f6 = new Float(f2);
        Float f7 = new Float(f3);
        Float f8 = new Float(f4);
        this.model = new SpinnerNumberModel(f5, f6, f7, f8);
        this.setModel(this.model);
    }

    public void setLimit(float f, float f2) {
        this.minValue = f;
        this.maxValue = f2;
        float f3 = this.get();
        Float f4 = new Float(f);
        Float f5 = new Float(f2);
        Float f6 = new Float(this.incValue);
        this.defValue = f3 > f2 ? f2 : (f3 < f ? f : f3);
        Float f7 = new Float(this.defValue);
        this.model = new SpinnerNumberModel(f7, f4, f5, f6);
        this.setModel(this.model);
    }

    public void setIncrement(float f) {
        this.incValue = f;
        Float f2 = (Float)this.getModel().getValue();
        Float f3 = new Float(this.minValue);
        Float f4 = new Float(this.maxValue);
        Float f5 = new Float(f);
        this.model = new SpinnerNumberModel(f2, f3, f4, f5);
        this.setModel(this.model);
    }

    public float getIncrement() {
        return this.incValue;
    }

    public void set(float f) {
        f = f > this.maxValue ? this.maxValue : (f < this.minValue ? this.minValue : f);
        this.model.setValue(Float.valueOf(f));
    }

    public float get() {
        if (this.model.getValue() instanceof Integer) {
            Integer n = (Integer)this.model.getValue();
            return n.intValue();
        }
        if (this.model.getValue() instanceof Double) {
            Double d = (Double)this.model.getValue();
            return (float)d.doubleValue();
        }
        if (this.model.getValue() instanceof Float) {
            Float f = (Float)this.model.getValue();
            return f.floatValue();
        }
        return 0.0f;
    }
}

