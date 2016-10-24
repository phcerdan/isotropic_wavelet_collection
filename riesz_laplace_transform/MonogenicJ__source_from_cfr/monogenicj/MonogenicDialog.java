/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  ij.IJ
 *  ij.ImagePlus
 *  ij.ImageStack
 *  ij.WindowManager
 *  ij.gui.GUI
 *  ij.process.ImageProcessor
 */
package monogenicj;

import additionaluserinterface.Chrono;
import additionaluserinterface.GridPanel;
import additionaluserinterface.GridToolbar;
import additionaluserinterface.Settings;
import additionaluserinterface.SpinnerDouble;
import additionaluserinterface.SpinnerInteger;
import additionaluserinterface.WalkBar;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.gui.GUI;
import ij.process.ImageProcessor;
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
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import monogenicj.MonogenicImage;
import steerabletools.DisplayPyramid;

public class MonogenicDialog
extends JDialog
implements ActionListener,
WindowListener,
Runnable {
    private Settings settings = new Settings("MonogenicJ", IJ.getDirectory((String)"plugins") + "MonogenicJ.txt");
    private Thread thread = null;
    private JButton job = null;
    private String[] list = new String[]{"Input", "Laplace", "Riesz X", "Riesz Y", "Orientation", "Coherency", "Energy", "Wavenumber", "Modulus", "Phase", "Dir. Hilbert", "Maximum"};
    private WalkBar walk = new WalkBar("(c) 2009 EPFL, BIG", true, false, true, 40);
    private SpinnerDouble spnSigma = new SpinnerDouble(3.0, 0.0, 100.0, 1.0);
    private SpinnerInteger spnScale = new SpinnerInteger(2, 1, 16, 1);
    private SpinnerDouble spnEpsilon = new SpinnerDouble(1.0, 0.0, 32.0, 1.0);
    private JButton bnRun = new JButton("Run");
    private JCheckBox ckSignedDir = new JCheckBox("Signed Dir.", false);
    private JCheckBox ckPrefilter = new JCheckBox("Prefilter", false);
    private JCheckBox ckGradientX = new JCheckBox("Riesz X", false);
    private JCheckBox ckGradientY = new JCheckBox("Riesz Y", false);
    private JCheckBox ckOrientation = new JCheckBox("Orientation", true);
    private JCheckBox ckCoherency = new JCheckBox("Coherency", true);
    private JCheckBox ckEnergy = new JCheckBox("Energy", false);
    private JCheckBox ckFrequency = new JCheckBox("Wavenumber", true);
    private JCheckBox ckDirHilbert = new JCheckBox("Dir. Hilbert", true);
    private JCheckBox ckModulus = new JCheckBox("Modulus", true);
    private JCheckBox ckPhase = new JCheckBox("Phase", true);
    private JCheckBox ckLaplace = new JCheckBox("Laplace", true);
    private JRadioButton rbPyramid = new JRadioButton("Pyramid", true);
    private JRadioButton rbRedundant = new JRadioButton("Redundant", true);
    private JCheckBox ckColor = new JCheckBox("Color map", true);
    private JComboBox cmbHue = new JComboBox<String>(this.list);
    private JComboBox cmbSat = new JComboBox<String>(this.list);
    private JComboBox cmbBri = new JComboBox<String>(this.list);
    private JComboBox cmbScaled = new JComboBox<String>(new String[]{"Scaled values / bands", "True values"});
    private JComboBox cmbStacked = new JComboBox<String>(new String[]{"Stacked presentation", "Horizontal Flatten", "Vertical Flatten"});
    private JButton showGradientX = new JButton(" Show ");
    private JButton showGradientY = new JButton(" Show ");
    private JButton showOrientat = new JButton(" Show ");
    private JButton showCoherency = new JButton(" Show ");
    private JButton showEnergy = new JButton(" Show ");
    private JButton showOrientation = new JButton(" Show ");
    private JButton showFrequency = new JButton(" Show ");
    private JButton showDirHilbert = new JButton(" Show ");
    private JButton showModulus = new JButton(" Show ");
    private JButton showPhase = new JButton(" Show ");
    private JButton showLaplace = new JButton(" Show ");
    private JButton showColor = new JButton(" Show ");
    private MonogenicImage mgim;

    public MonogenicDialog() {
        super(new Frame(), "MonogenicJ");
        this.walk.fillAbout("MonogenicJ", "Version 21.09.2009", "Reference: M. Unser, D. Sage, D. Van De Ville, Multiresolution Monogenic Signal Analysis Using the Riesz-Laplace Wavelet Transform, IEEE Transactions on Image Processing, in press.", "", "Biomedical Imaging Group (BIG)<br>Ecole Polytechnique F&eacute;d&eacute;rale de Lausanne (EPFL)<br>Lausanne, Switzerland", "24 January 2009", "http://bigwww.epfl.ch/demo/monogenic/");
        this.doDialog();
        this.cmbHue.setSelectedIndex(3);
        this.cmbSat.setSelectedIndex(4);
        this.settings.record("MonogenicJ-ckGradientX", this.ckGradientX, false);
        this.settings.record("MonogenicJ-ckGradientY", this.ckGradientY, false);
        this.settings.record("MonogenicJ-ckOrientation", this.ckOrientation, false);
        this.settings.record("MonogenicJ-ckCoherency", this.ckCoherency, false);
        this.settings.record("MonogenicJ-ckEnergy", this.ckEnergy, false);
        this.settings.record("MonogenicJ-ckColor", this.ckColor, true);
        this.settings.record("MonogenicJ-ckOrientation", this.ckOrientation, false);
        this.settings.record("MonogenicJ-ckFrequency", this.ckFrequency, false);
        this.settings.record("MonogenicJ-ckDirHilbert", this.ckDirHilbert, false);
        this.settings.record("MonogenicJ-ckModulus", this.ckModulus, false);
        this.settings.record("MonogenicJ-ckPhase", this.ckPhase, false);
        this.settings.record("MonogenicJ-ckLaplace", this.ckLaplace, false);
        this.settings.record("MonogenicJ-cmbHue", this.cmbHue, "Orientation");
        this.settings.record("MonogenicJ-cmbSat", this.cmbSat, "Coherency");
        this.settings.record("MonogenicJ-cmbBri", this.cmbBri, "Input");
        this.settings.record("MonogenicJ-spnSigma", this.spnSigma, "1");
        this.settings.record("MonogenicJ-spnScale", this.spnScale, "1");
        this.settings.record("MonogenicJ-spnEpsilon", this.spnEpsilon, "1");
        this.settings.record("MonogenicJ-ckPyramid", this.rbPyramid, true);
        this.settings.record("MonogenicJ-rbRedundant", this.rbRedundant, false);
        this.settings.record("MonogenicJ-cmbScaled", this.cmbScaled, "True values");
        this.settings.record("MonogenicJ-cmbStacked", this.cmbStacked, "Stacked presentation");
        this.settings.record("MonogenicJ-ckPrefilter", this.ckPrefilter, false);
        this.settings.record("MonogenicJ-ckSignedDir", this.ckSignedDir, false);
        this.settings.loadRecordedItems();
    }

    private void doDialog() {
        JLabel lblRiesz = new JLabel("Monogenic Components");
        JLabel lblTensor = new JLabel("Structure Tensor");
        JLabel lblMonogenic = new JLabel("Monogenic Analysis");
        lblRiesz.setBorder(BorderFactory.createEtchedBorder());
        lblTensor.setBorder(BorderFactory.createEtchedBorder());
        lblMonogenic.setBorder(BorderFactory.createEtchedBorder());
        lblRiesz.setAlignmentX(0.5f);
        ButtonGroup group = new ButtonGroup();
        group.add(this.rbPyramid);
        group.add(this.rbRedundant);
        GridPanel pn0 = new GridPanel(false);
        pn0.place(0, 0, this.rbPyramid);
        pn0.place(0, 1, this.rbRedundant);
        pn0.place(1, 0, new JLabel("Nb of Scale"));
        pn0.place(1, 1, this.spnScale);
        pn0.place(3, 0, new JLabel("Sigma [Tensor]"));
        pn0.place(3, 1, this.spnSigma);
        GridPanel pnRun = new GridPanel(false, 0);
        pnRun.place(2, 2, 1, 1, this.bnRun);
        GridToolbar pn1 = new GridToolbar("Features");
        pn1.place(1, 0, 2, 1, lblRiesz);
        pn1.place(2, 0, this.ckLaplace);
        pn1.place(3, 0, this.ckGradientX);
        pn1.place(4, 0, this.ckGradientY);
        pn1.place(6, 0, 2, 1, lblTensor);
        pn1.place(7, 0, this.ckOrientation);
        pn1.place(8, 0, this.ckCoherency);
        pn1.place(9, 0, this.ckEnergy);
        pn1.place(12, 0, 2, 1, lblMonogenic);
        pn1.place(14, 0, this.ckFrequency);
        pn1.place(15, 0, this.ckModulus);
        pn1.place(16, 0, this.ckPhase);
        pn1.place(17, 0, this.ckDirHilbert);
        pn1.place(2, 1, this.showLaplace);
        pn1.place(3, 1, this.showGradientX);
        pn1.place(4, 1, this.showGradientY);
        pn1.place(7, 1, this.showOrientation);
        pn1.place(8, 1, this.showCoherency);
        pn1.place(9, 1, this.showEnergy);
        pn1.place(14, 1, this.showFrequency);
        pn1.place(15, 1, this.showModulus);
        pn1.place(16, 1, this.showPhase);
        pn1.place(17, 1, this.showDirHilbert);
        pn1.place(20, 0, 2, 1, this.cmbScaled);
        pn1.place(21, 0, 2, 1, this.cmbStacked);
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
        this.showFrequency.addActionListener(this);
        this.showDirHilbert.addActionListener(this);
        this.showModulus.addActionListener(this);
        this.showPhase.addActionListener(this);
        this.showLaplace.addActionListener(this);
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
            return;
        }
        if (e.getSource() == this.bnRun) {
            this.job = (JButton)e.getSource();
            if (this.thread == null) {
                this.thread = new Thread(this);
                this.thread.setPriority(1);
                this.thread.start();
            }
            return;
        }
        if (this.mgim == null) {
            IJ.error((String)"Run first the algorithm before to show the result");
            return;
        }
        this.showFeatures((JButton)e.getSource());
        this.notify();
    }

    public void run() {
        ImagePlus imp;
        Chrono.tic();
        if (this.job == this.bnRun && (imp = this.getInputImage()) != null) {
            int scale = this.spnScale.get();
            boolean pyramid = this.rbPyramid.isSelected();
            this.mgim = new MonogenicImage(this.walk, imp.getProcessor(), scale, pyramid, this.spnSigma.get());
            this.mgim.compute(this.spnSigma.get(), 1.0E-7, this.ckPrefilter.isSelected(), this.ckSignedDir.isSelected());
            if (this.ckLaplace.isSelected()) {
                this.showFeatures(this.showLaplace);
            }
            if (this.ckGradientX.isSelected()) {
                this.showFeatures(this.showGradientX);
            }
            if (this.ckGradientY.isSelected()) {
                this.showFeatures(this.showGradientY);
            }
            if (this.ckOrientation.isSelected()) {
                this.showFeatures(this.showOrientation);
            }
            if (this.ckCoherency.isSelected()) {
                this.showFeatures(this.showCoherency);
            }
            if (this.ckEnergy.isSelected()) {
                this.showFeatures(this.showEnergy);
            }
            if (this.ckFrequency.isSelected()) {
                this.showFeatures(this.showFrequency);
            }
            if (this.ckDirHilbert.isSelected()) {
                this.showFeatures(this.showDirHilbert);
            }
            if (this.ckModulus.isSelected()) {
                this.showFeatures(this.showModulus);
            }
            if (this.ckPhase.isSelected()) {
                this.showFeatures(this.showPhase);
            }
            if (this.ckColor.isSelected()) {
                this.showFeatures(this.showColor);
            }
        }
        this.walk.setMessage(Chrono.toc("End:"));
        this.thread = null;
    }

    private void showFeatures(JButton bn) {
        int scaled = this.cmbScaled.getSelectedIndex();
        int stacked = this.cmbStacked.getSelectedIndex();
        if (bn == this.showGradientX) {
            DisplayPyramid.show(this.mgim.rx, "Riesz X", scaled, stacked, this.mgim.pyramid);
        } else if (bn == this.showGradientY) {
            DisplayPyramid.show(this.mgim.ry, "Riesz Y", scaled, stacked, this.mgim.pyramid);
        } else if (bn == this.showOrientation) {
            DisplayPyramid.show(this.mgim.orientation, "Orientation", DisplayPyramid.NORESCALE, stacked, this.mgim.pyramid);
        } else if (bn == this.showCoherency) {
            DisplayPyramid.show(this.mgim.coherency, "Coherency", scaled, stacked, this.mgim.pyramid);
        } else if (bn == this.showEnergy) {
            DisplayPyramid.show(this.mgim.energy, "Energy", scaled, stacked, this.mgim.pyramid);
        } else if (bn == this.showFrequency) {
            DisplayPyramid.show(this.mgim.monogenicFrequency, "Monogenic Wavenumber", scaled, stacked, this.mgim.pyramid);
        } else if (bn == this.showDirHilbert) {
            DisplayPyramid.show(this.mgim.directionalHilbert, "Directional Hilbert", scaled, stacked, this.mgim.pyramid);
        } else if (bn == this.showModulus) {
            DisplayPyramid.show(this.mgim.monogenicModulus, "Monogenic Modulus", scaled, stacked, this.mgim.pyramid);
        } else if (bn == this.showPhase) {
            DisplayPyramid.show(this.mgim.monogenicPhase, "Monogenic Phase", DisplayPyramid.NORESCALE, stacked, this.mgim.pyramid);
        } else if (bn == this.showLaplace) {
            DisplayPyramid.show(this.mgim.laplace, "Laplace", scaled, stacked, this.mgim.pyramid);
        } else if (bn == this.showColor) {
            ImageWare hue = this.selectColor(this.cmbHue);
            ImageWare sat = this.selectColor(this.cmbSat);
            ImageWare bri = this.selectColor(this.cmbBri);
            DisplayPyramid.colorHSB("Color Survey", hue, sat, bri, stacked, this.mgim.pyramid);
        }
    }

    private ImageWare selectColor(JComboBox cmb) {
        ImageWare out = null;
        String item = (String)cmb.getSelectedItem();
        if (item.equals("Laplace")) {
            out = this.mgim.laplace.convert(3);
            out.rescale(0.0, 1.0);
        } else if (item.equals("Riesz X")) {
            out = this.mgim.rx.duplicate();
            out.rescale(0.0, 1.0);
        } else if (item.equals("Riesz Y")) {
            out = this.mgim.ry.duplicate();
            out.rescale(0.0, 1.0);
        } else if (item.equals("Orientation")) {
            out = DisplayPyramid.rescaleAngle(this.mgim.orientation, this.mgim.pyramid);
        } else if (item.equals("Coherency")) {
            out = this.mgim.coherency.duplicate();
        } else if (item.equals("Energy")) {
            out = this.mgim.energy.duplicate();
            out.rescale(0.0, 1.0);
        } else if (item.equals("Wavenumber")) {
            out = this.mgim.monogenicFrequency.convert(3);
            out.rescale(0.0, 1.0);
        } else if (item.equals("Modulus")) {
            out = this.mgim.monogenicModulus.convert(3);
            out.rescale(0.0, 1.0);
        } else if (item.equals("Phase")) {
            out = DisplayPyramid.rescaleAngle(this.mgim.monogenicPhase, this.mgim.pyramid);
        } else if (item.equals("Dir. Hilbert")) {
            out = this.mgim.directionalHilbert.convert(3);
            out.rescale(0.0, 1.0);
        } else if (item.equals("Maximum")) {
            out = Builder.create(this.mgim.nx, this.mgim.ny, this.mgim.scale, 3);
            out.fillConstant(1.0);
        } else {
            return this.mgim.sourceColorChannel;
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
        if (imp.getStack().getSize() > 1) {
            IJ.error((String)"Do not processed stack of images.");
            return null;
        }
        int m = 1;
        for (int s = 0; s < this.spnScale.get(); ++s) {
            m *= 2;
        }
        int nx = imp.getWidth();
        int ny = imp.getHeight();
        if (nx % m != 0) {
            IJ.error((String)("The width [" + nx + "] of the input image is not a multiple of 2^scale [" + m + "]."));
            return null;
        }
        if (ny % m != 0) {
            IJ.error((String)("The height  [" + ny + "] of the input image is not a multiple of 2^scale [" + m + "]."));
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

