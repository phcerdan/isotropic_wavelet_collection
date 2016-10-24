/*
 * Decompiled with CFR 0_118.
 */
package additionaluserinterface;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class GridPanel
extends JPanel {
    private GridBagLayout layout = new GridBagLayout();
    private GridBagConstraints constraint = new GridBagConstraints();
    private int defaultSpace = 3;

    public GridPanel() {
        this.setLayout(this.layout);
        this.setBorder(BorderFactory.createEtchedBorder());
    }

    public GridPanel(int n) {
        this.setLayout(this.layout);
        this.defaultSpace = n;
        this.setBorder(BorderFactory.createEtchedBorder());
    }

    public GridPanel(boolean bl) {
        this.setLayout(this.layout);
        if (bl) {
            this.setBorder(BorderFactory.createEtchedBorder());
        }
    }

    public GridPanel(String string) {
        this.setLayout(this.layout);
        this.setBorder(BorderFactory.createTitledBorder(string));
    }

    public GridPanel(boolean bl, int n) {
        this.setLayout(this.layout);
        this.defaultSpace = n;
        if (bl) {
            this.setBorder(BorderFactory.createEtchedBorder());
        }
    }

    public GridPanel(String string, int n) {
        this.setLayout(this.layout);
        this.defaultSpace = n;
        this.setBorder(BorderFactory.createTitledBorder(string));
    }

    public void setSpace(int n) {
        this.defaultSpace = n;
    }

    public void place(int n, int n2, JComponent jComponent) {
        this.place(n, n2, 1, 1, this.defaultSpace, jComponent);
    }

    public void place(int n, int n2, int n3, JComponent jComponent) {
        this.place(n, n2, 1, 1, n3, jComponent);
    }

    public void place(int n, int n2, int n3, int n4, JComponent jComponent) {
        this.place(n, n2, n3, n4, this.defaultSpace, jComponent);
    }

    public void place(int n, int n2, int n3, int n4, int n5, JComponent jComponent) {
        this.constraint.gridx = n2;
        this.constraint.gridy = n;
        this.constraint.gridwidth = n3;
        this.constraint.gridheight = n4;
        this.constraint.anchor = 18;
        this.constraint.insets = new Insets(n5, n5, n5, n5);
        this.constraint.fill = 2;
        this.layout.setConstraints(jComponent, this.constraint);
        this.add(jComponent);
    }
}

