/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 *  ij.gui.GUI
 */
package steerabletools;

import additionaluserinterface.GridPanel;
import additionaluserinterface.Settings;
import additionaluserinterface.SpinnerInteger;
import additionaluserinterface.WalkBar;
import ij.ImagePlus;
import ij.gui.GUI;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import polyharmonicwavelets.Autocorrelation;
import polyharmonicwavelets.ComplexImage;

public class AutocorrelationDialog
extends JDialog
implements ActionListener,
WindowListener,
Runnable {
    private Settings settings = null;
    private WalkBar walk = null;
    private Thread thread = null;
    private JButton job = null;
    private SpinnerInteger spnOrder = new SpinnerInteger(3, 0, 100, 1);
    private JTextField txtNX = new JTextField("256");
    private JTextField txtNY = new JTextField("256");
    private JTextField txtGamma = new JTextField("---");
    private JTextField txtIterative = new JTextField("---");
    private JButton bnIterative = new JButton("Compute");
    private JButton bnGamma = new JButton("Compute");
    private ImagePlus imp;

    public AutocorrelationDialog(ImagePlus imp, String title, Settings settings, WalkBar walk) {
        super(new Frame(), "Autocorrelation");
        this.imp = imp;
        this.settings = settings;
        this.walk = walk;
        this.doDialog(imp);
        settings.record("Autocorrelation-spnOrder", this.spnOrder, "3");
        settings.loadRecordedItems();
    }

    private void doDialog(ImagePlus imp) {
        if (imp == null) {
            this.txtNX.setText("256");
            this.txtNY.setText("256");
        } else {
            this.txtNX.setText("" + imp.getWidth());
            this.txtNY.setText("" + imp.getHeight());
        }
        GridPanel pn0 = new GridPanel(false);
        pn0.place(0, 0, new JLabel("Order"));
        pn0.place(0, 1, this.spnOrder);
        pn0.place(1, 0, new JLabel("Width"));
        pn0.place(1, 1, this.txtNX);
        pn0.place(2, 0, new JLabel("Height"));
        pn0.place(2, 1, this.txtNY);
        pn0.place(3, 0, new JLabel("Iterative"));
        pn0.place(3, 1, this.txtIterative);
        pn0.place(3, 2, this.bnIterative);
        pn0.place(4, 0, new JLabel("Gamma"));
        pn0.place(4, 1, this.txtGamma);
        pn0.place(4, 2, this.bnGamma);
        GridPanel panel = new GridPanel(false, 8);
        panel.place(0, 0, pn0);
        panel.place(1, 0, this.walk);
        this.walk.getButtonClose().addActionListener(this);
        this.bnIterative.addActionListener(this);
        this.bnGamma.addActionListener(this);
        this.addWindowListener(this);
        this.add(panel);
        this.setResizable(true);
        this.pack();
        GUI.center((Window)this);
        this.setVisible(true);
    }

    public synchronized void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Close")) {
            this.settings.storeRecordedItems();
            this.dispose();
        } else if (e.getSource() == this.bnGamma) {
            this.job = this.bnGamma;
            if (this.thread == null) {
                this.thread = new Thread(this);
                this.thread.setPriority(1);
                this.thread.start();
            }
        } else if (e.getSource() == this.bnIterative) {
            this.job = this.bnIterative;
            if (this.thread == null) {
                this.thread = new Thread(this);
                this.thread.setPriority(1);
                this.thread.start();
            }
        }
        this.notify();
    }

    public void run() {
        double chrono = System.currentTimeMillis();
        int nx = Integer.parseInt(this.txtNX.getText());
        int ny = Integer.parseInt(this.txtNY.getText());
        int order = this.spnOrder.get();
        Autocorrelation ac = new Autocorrelation(nx, ny, order);
        if (this.job == this.bnGamma) {
            ComplexImage autocorrelation = ac.computeGamma(true);
            this.txtGamma.setText("" + ((double)System.currentTimeMillis() - chrono) + " ms");
            autocorrelation.showReal("Autocorrelation Gamma");
        } else if (this.job == this.bnIterative) {
            ComplexImage autocorrelation = ac.computeIterative(true);
            this.txtIterative.setText("" + ((double)System.currentTimeMillis() - chrono) + " ms");
            autocorrelation.showReal("Autocorrelation Iterative");
        }
        this.thread = null;
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        this.dispose();
    }
}

