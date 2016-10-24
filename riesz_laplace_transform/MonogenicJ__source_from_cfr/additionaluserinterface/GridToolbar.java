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
import javax.swing.JToolBar;
import javax.swing.border.Border;

public class GridToolbar
extends JToolBar {
    private GridBagLayout layout = new GridBagLayout();
    private GridBagConstraints constraint = new GridBagConstraints();
    private int defaultSpace = 3;

    public GridToolbar() {
        super("Control");
        this.setLayout(this.layout);
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setFloatable(false);
    }

    public GridToolbar(boolean bl) {
        super("Control");
        this.setLayout(this.layout);
        if (bl) {
            this.setBorder(BorderFactory.createEtchedBorder());
        }
        this.setFloatable(false);
    }

    public GridToolbar(String string) {
        super(string);
        this.setLayout(this.layout);
        this.setBorder(BorderFactory.createTitledBorder(string));
        this.setFloatable(false);
    }

    public GridToolbar(int n) {
        super("Control");
        this.setLayout(this.layout);
        this.defaultSpace = n;
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setFloatable(false);
    }

    public GridToolbar(boolean bl, int n) {
        super("Control");
        this.setLayout(this.layout);
        this.defaultSpace = n;
        if (bl) {
            this.setBorder(BorderFactory.createEtchedBorder());
        }
        this.setFloatable(false);
    }

    public GridToolbar(boolean bl, int n, boolean bl2) {
        super("Control");
        this.setLayout(this.layout);
        this.defaultSpace = n;
        if (bl) {
            this.setBorder(BorderFactory.createEtchedBorder());
        }
        this.setFloatable(bl2);
    }

    public GridToolbar(boolean bl, boolean bl2) {
        super("Control");
        this.setLayout(this.layout);
        if (bl) {
            this.setBorder(BorderFactory.createEtchedBorder());
        }
        this.setFloatable(bl2);
    }

    public GridToolbar(String string, boolean bl) {
        super(string);
        this.setLayout(this.layout);
        this.setBorder(BorderFactory.createTitledBorder(string));
        this.setFloatable(bl);
    }

    public GridToolbar(int n, boolean bl) {
        super("Control");
        this.setLayout(this.layout);
        this.defaultSpace = n;
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setFloatable(bl);
    }

    public GridToolbar(String string, int n) {
        super(string);
        this.setLayout(this.layout);
        this.defaultSpace = n;
        this.setBorder(BorderFactory.createTitledBorder(string));
        this.setFloatable(false);
    }

    public GridToolbar(String string, int n, boolean bl) {
        super(string);
        this.setLayout(this.layout);
        this.defaultSpace = n;
        this.setBorder(BorderFactory.createTitledBorder(string));
        this.setFloatable(bl);
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

