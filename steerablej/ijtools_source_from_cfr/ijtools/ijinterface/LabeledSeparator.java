/*
 * Decompiled with CFR 0_118.
 */
package ijtools.ijinterface;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class LabeledSeparator
extends JPanel {
    public LabeledSeparator(String string) {
        this.setLayout(new GridBagLayout());
        this.add((Component)new JLabel(string), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.add((Component)new JSeparator(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 17, 2, new Insets(0, 2, 0, 0), 0, 0));
    }
}

