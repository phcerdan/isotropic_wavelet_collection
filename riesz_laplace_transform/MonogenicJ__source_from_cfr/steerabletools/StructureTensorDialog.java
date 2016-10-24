/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.IJ
 *  ij.ImagePlus
 *  ij.WindowManager
 *  ij.gui.GUI
 */
package steerabletools;

import additionaluserinterface.Chrono;
import additionaluserinterface.GridPanel;
import additionaluserinterface.GridToolbar;
import additionaluserinterface.Settings;
import additionaluserinterface.SpinnerDouble;
import additionaluserinterface.WalkBar;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GUI;
import imageware.Builder;
import imageware.ImageWare;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import steerabletools.DisplayPyramid;
import steerabletools.Gradient;
import steerabletools.StructureTensor;

public class StructureTensorDialog
extends JDialog
implements ActionListener,
WindowListener,
Runnable {
    private Settings settings;
    private WalkBar walk;
    private Thread thread = null;
    private JButton job = null;
    private String[] list = new String[]{"Input", "Gradient X", "Gradient Y", "Orientation", "Coherency", "Energy", "Maximum"};
    private SpinnerDouble spnSigma = new SpinnerDouble(3.0, 0.0, 100.0, 1.0);
    private SpinnerDouble spnEpsilon = new SpinnerDouble(1.0, 0.0, 32.0, 1.0);
    private JButton bnRun = new JButton("Run");
    private JCheckBox ckGradientX = new JCheckBox("Gradient X", false);
    private JCheckBox ckGradientY = new JCheckBox("Gradient Y", false);
    private JCheckBox ckOrientation = new JCheckBox("Orientation", true);
    private JCheckBox ckCoherency = new JCheckBox("Coherency", true);
    private JCheckBox ckEnergy = new JCheckBox("Energy", false);
    private JCheckBox ckColor = new JCheckBox("Color map", true);
    private JComboBox cmbGradient = new JComboBox<String>(new String[]{"Finite Difference", "Cubic Spline", "Riesz Filter"});
    private JComboBox cmbHue = new JComboBox<String>(this.list);
    private JComboBox cmbSat = new JComboBox<String>(this.list);
    private JComboBox cmbBri = new JComboBox<String>(this.list);
    private JButton showGradientX = new JButton(" Show ");
    private JButton showGradientY = new JButton(" Show ");
    private JButton showOrientation = new JButton(" Show ");
    private JButton showCoherency = new JButton(" Show ");
    private JButton showEnergy = new JButton(" Show ");
    private JButton showColor = new JButton(" Show ");
    private StructureTensor tensor;
    private Gradient gradient;
    private ImageWare input;

    public StructureTensorDialog(String title, Settings settings, WalkBar walk) {
        super(new Frame(), title);
        this.settings = settings;
        this.walk = walk;
        this.doDialog();
        this.cmbHue.setSelectedIndex(3);
        this.cmbSat.setSelectedIndex(4);
        settings.record("StructureTensor-ckGradientX", this.ckGradientX, false);
        settings.record("StructureTensor-ckGradientY", this.ckGradientY, false);
        settings.record("StructureTensor-ckOrientation", this.ckOrientation, false);
        settings.record("StructureTensor-ckCoherency", this.ckCoherency, false);
        settings.record("StructureTensor-ckEnergy", this.ckEnergy, false);
        settings.record("StructureTensor-ckColor", this.ckColor, true);
        settings.record("StructureTensor-cmbGradient", this.cmbGradient, "Riesz Filter");
        settings.record("StructureTensor-cmbHue", this.cmbHue, "Orientation");
        settings.record("StructureTensor-cmbSat", this.cmbSat, "Coherency");
        settings.record("StructureTensor-cmbBri", this.cmbBri, "Input");
        settings.record("StructureTensor-spnSigma", this.spnSigma, "1");
        settings.record("StructureTensor-spnEpsilon", this.spnEpsilon, "1");
        settings.loadRecordedItems();
    }

    private void doDialog() {
        GridPanel pn0 = new GridPanel(false);
        pn0.place(0, 0, 1, 1, new JLabel("Gradient"));
        pn0.place(0, 1, 3, 1, this.cmbGradient);
        pn0.place(1, 0, 2, 1, new JLabel("Sigma"));
        pn0.place(1, 2, 1, 1, this.spnSigma);
        pn0.place(1, 3, 1, 1, new JLabel("[pixel]"));
        pn0.place(2, 0, 2, 1, new JLabel("Epsilon"));
        pn0.place(2, 2, 1, 1, this.spnEpsilon);
        pn0.place(2, 3, 1, 1, new JLabel("x 10e-6"));
        GridPanel pnRun = new GridPanel(false, 0);
        pnRun.place(2, 2, 1, 1, this.bnRun);
        GridToolbar pn1 = new GridToolbar("Features");
        pn1.place(0, 0, this.ckGradientX);
        pn1.place(1, 0, this.ckGradientY);
        pn1.place(2, 0, this.ckOrientation);
        pn1.place(3, 0, this.ckCoherency);
        pn1.place(4, 0, this.ckEnergy);
        pn1.place(0, 1, this.showGradientX);
        pn1.place(1, 1, this.showGradientY);
        pn1.place(2, 1, this.showOrientation);
        pn1.place(3, 1, this.showCoherency);
        pn1.place(4, 1, this.showEnergy);
        GridToolbar pn2 = new GridToolbar(false);
        pn2.place(0, 0, new JLabel("Hue"));
        pn2.place(1, 0, new JLabel("Saturation"));
        pn2.place(2, 0, new JLabel("Brightness"));
        pn2.place(0, 2, this.cmbHue);
        pn2.place(1, 2, this.cmbSat);
        pn2.place(2, 2, this.cmbBri);
        pn2.place(5, 0, this.ckColor);
        pn2.place(5, 2, this.showColor);
        GridToolbar pn3 = new GridToolbar();
        this.cmbHue.setFont(this.showGradientX.getFont());
        this.cmbSat.setFont(this.showGradientX.getFont());
        this.cmbBri.setFont(this.showGradientX.getFont());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Parameters", pn0);
        tabbedPane.addTab("Color Map", pn2);
        GridPanel panel = new GridPanel(false, 5);
        panel.place(1, 0, pn1);
        panel.place(2, 0, tabbedPane);
        panel.place(3, 0, 1, 1, 0, pnRun);
        panel.place(4, 0, this.walk);
        this.walk.getButtonClose().addActionListener(this);
        this.showGradientX.addActionListener(this);
        this.showGradientY.addActionListener(this);
        this.showOrientation.addActionListener(this);
        this.showCoherency.addActionListener(this);
        this.showEnergy.addActionListener(this);
        this.showColor.addActionListener(this);
        this.bnRun.addActionListener(this);
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
        } else if (e.getSource() == this.showGradientX) {
            if (this.gradient != null) {
                this.gradient.getGradientX().show("Gradient X");
            } else {
                IJ.error((String)"Run first the algorithm before to show the result");
            }
        } else if (e.getSource() == this.showGradientY) {
            if (this.gradient != null) {
                this.gradient.getGradientY().show("Gradient Y");
            } else {
                IJ.error((String)"Run first the algorithm before to show the result");
            }
        } else if (e.getSource() == this.showOrientation) {
            if (this.tensor != null) {
                this.tensor.getOrientation().show("Orientation");
            } else {
                IJ.error((String)"Run first the algorithm before to show the result");
            }
        } else if (e.getSource() == this.showCoherency) {
            if (this.tensor != null) {
                this.tensor.getCoherency().show("Coherency");
            } else {
                IJ.error((String)"Run first the algorithm before to show the result");
            }
        } else if (e.getSource() == this.showEnergy) {
            if (this.tensor != null) {
                this.tensor.getEnergy().show("Energy");
            } else {
                IJ.error((String)"Run first the algorithm before to show the result");
            }
        } else if (e.getSource() == this.showColor) {
            if (this.tensor != null) {
                this.showColor();
            } else {
                IJ.error((String)"Run first the algorithm before to show the result");
            }
        } else if (e.getSource() == this.bnRun) {
            this.job = (JButton)e.getSource();
            if (this.thread == null) {
                this.thread = new Thread(this);
                this.thread.setPriority(1);
                this.thread.start();
            }
        }
        this.notify();
    }

    public synchronized void statesChanged(ChangeEvent e) {
        this.notify();
    }

    public void run() {
        ImagePlus imp;
        Chrono.tic();
        if (this.job == this.bnRun && (imp = this.getInputImage()) != null) {
            this.input = Builder.wrap(imp);
            this.gradient = null;
            System.gc();
            this.gradient = new Gradient(this.walk);
            switch (this.cmbGradient.getSelectedIndex()) {
                case 0: {
                    this.gradient.computeFiniteDifference(this.input);
                    break;
                }
                case 1: {
                    this.gradient.computeCubicSpline(this.input);
                    break;
                }
                case 2: {
                    this.gradient.computeRieszFilter(this.input);
                }
            }
            if (this.ckGradientX.isSelected()) {
                this.gradient.getGradientX().show("Gradient in X");
            }
            if (this.ckGradientY.isSelected()) {
                this.gradient.getGradientY().show("Gradient in Y");
            }
            this.tensor = null;
            System.gc();
            this.tensor = new StructureTensor(this.walk);
            this.tensor.compute(this.gradient.getGradientX(), this.gradient.getGradientY(), this.spnSigma.get(), this.spnEpsilon.get() * 1.0E-5);
            if (this.ckOrientation.isSelected()) {
                this.tensor.getOrientation().show("Orientation");
            }
            if (this.ckEnergy.isSelected()) {
                this.tensor.getEnergy().show("Energy");
            }
            if (this.ckCoherency.isSelected()) {
                this.tensor.getCoherency().show("Coherency");
            }
            if (this.ckColor.isSelected()) {
                this.showColor();
            }
        }
        this.walk.setMessage(Chrono.toc("End:"));
        this.thread = null;
    }

    private void showColor() {
        ImageWare hue = this.selectColor(this.cmbHue);
        ImageWare sat = this.selectColor(this.cmbSat);
        ImageWare bri = this.selectColor(this.cmbBri);
        DisplayPyramid.colorHSB("Color Survey", hue, sat, bri, 0, false);
    }

    private ImageWare selectColor(JComboBox cmb) {
        ImageWare out = null;
        if (cmb.getSelectedItem().equals("Gradient X")) {
            out = this.gradient.getGradientX().duplicate();
            out.rescale(0.0, 1.0);
        }
        if (cmb.getSelectedItem().equals("Gradient Y")) {
            out = this.gradient.getGradientY().duplicate();
            out.rescale(0.0, 1.0);
        }
        if (cmb.getSelectedItem().equals("Orientation")) {
            out = this.tensor.getOrientation().duplicate();
            out.add(1.5707963267948966);
            out.divide(3.141592653589793);
        }
        if (cmb.getSelectedItem().equals("Coherency")) {
            out = this.tensor.getCoherency().duplicate();
        }
        if (cmb.getSelectedItem().equals("Energy")) {
            out = this.tensor.getEnergy().duplicate();
            out.rescale(0.0, 1.0);
        }
        if (cmb.getSelectedItem().equals("Input")) {
            out = this.input.convert(3);
            out.rescale(0.0, 1.0);
        }
        if (cmb.getSelectedItem().equals("Maximum")) {
            out = Builder.create(this.input.getSizeX(), this.input.getSizeY(), 1, 3);
            out.fillConstant(1.0);
        }
        if (out.getType() != 3) {
            return out.convert(3);
        }
        return out;
    }

    private ImagePlus getInputImage() {
        ImagePlus imp = WindowManager.getCurrentImage();
        if (imp == null) {
            IJ.error((String)"No open image.");
            return null;
        }
        if (imp.getType() != 0 && imp.getType() != 1 && imp.getType() != 2) {
            IJ.error((String)"Only processed 8-bits, 16-bits, or 32 bits images.");
            return null;
        }
        return imp;
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

