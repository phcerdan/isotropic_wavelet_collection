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
package steerabletools;

import additionaluserinterface.Chrono;
import additionaluserinterface.GridPanel;
import additionaluserinterface.Settings;
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
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;

public abstract class TransformDialog
extends JDialog
implements ActionListener,
WindowListener,
Runnable {
    protected Settings settings = null;
    private WalkBar walk = null;
    private Thread thread = null;
    private JButton job = null;
    private JButton bnAnalysis = new JButton("Analysis");
    private JButton bnSynthesis = new JButton("Synthesis");
    private JButton bnShowFilter = new JButton("Show Filters");
    private JButton bnShowCoef = new JButton("Show Coef.");
    private JButton bnRecons = new JButton("Check Perfect Reconstruction");

    public TransformDialog(String title, Settings settings, WalkBar walk) {
        super(new Frame(), title);
        this.settings = settings;
        this.walk = walk;
        this.doDialog();
        this.recordParameters();
        settings.loadRecordedItems();
    }

    public abstract void recordParameters();

    public abstract GridPanel createPanelParameters();

    public abstract void analysisAndShowCoef(ImageWare var1);

    public abstract void analysis(ImageWare var1);

    public abstract void synthesis(ImageWare[] var1);

    public abstract void showFilter(int var1, int var2);

    public abstract void checkPerfectReconstruction(ImageWare var1);

    private void doDialog() {
        GridPanel pn0 = new GridPanel(true);
        pn0.place(1, 0, 1, 1, this.bnAnalysis);
        pn0.place(1, 1, 1, 1, this.bnSynthesis);
        pn0.place(2, 0, 1, 1, this.bnShowFilter);
        pn0.place(2, 1, 1, 1, this.bnShowCoef);
        pn0.place(4, 0, 2, 1, this.bnRecons);
        GridPanel panel = new GridPanel(false, 5);
        panel.place(0, 0, this.createPanelParameters());
        panel.place(1, 0, pn0);
        panel.place(2, 0, this.walk);
        this.walk.getButtonClose().addActionListener(this);
        this.bnRecons.addActionListener(this);
        this.bnSynthesis.addActionListener(this);
        this.bnAnalysis.addActionListener(this);
        this.bnShowFilter.addActionListener(this);
        this.bnShowCoef.addActionListener(this);
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
        } else if (e.getSource() == this.bnAnalysis || e.getSource() == this.bnSynthesis || e.getSource() == this.bnShowFilter || e.getSource() == this.bnShowCoef || e.getSource() == this.bnRecons) {
            this.job = (JButton)e.getSource();
            if (this.thread == null) {
                this.thread = new Thread(this);
                this.thread.setPriority(1);
                this.thread.start();
            }
        }
        this.notify();
    }

    public void run() {
        ImagePlus imp;
        Chrono.tic();
        if (this.job == this.bnAnalysis) {
            ImagePlus imp2 = this.getCurrentImage();
            if (imp2 != null) {
                ImageWare input = Builder.wrap(imp2);
                this.analysis(input);
            }
        } else if (this.job == this.bnSynthesis) {
            ImagePlus imp3 = this.getCurrentStack();
            if (imp3 != null) {
                int n = imp3.getStack().getSize();
                ImageWare[] channels = new ImageWare[n];
                for (int s = 0; s < n; ++s) {
                    ImagePlus temp = new ImagePlus("", imp3.getStack().getProcessor(s + 1));
                    channels[s] = Builder.create(temp, 4);
                }
                this.synthesis(channels);
            }
        } else if (this.job == this.bnRecons) {
            ImagePlus imp4 = this.getCurrentImage();
            if (imp4 != null) {
                ImageWare input = Builder.wrap(imp4);
                this.checkPerfectReconstruction(input);
            }
        } else if (this.job == this.bnShowFilter) {
            ImagePlus imp5 = this.getCurrentImage();
            int nx = 256;
            int ny = 256;
            if (imp5 != null) {
                nx = imp5.getWidth();
                ny = imp5.getHeight();
            }
            this.showFilter(nx, ny);
        } else if (this.job == this.bnShowCoef && (imp = this.getCurrentImage()) != null) {
            ImageWare input = Builder.wrap(imp);
            this.analysisAndShowCoef(input);
        }
        this.walk.setMessage(Chrono.toc("End:"));
        this.thread = null;
    }

    private ImagePlus getCurrentImage() {
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
            IJ.error((String)"Do not process stack of images.");
            return null;
        }
        return imp;
    }

    private ImagePlus getCurrentStack() {
        ImagePlus imp = WindowManager.getCurrentImage();
        if (imp == null) {
            IJ.error((String)"No open image.");
            return null;
        }
        if (imp.getType() != 0 && imp.getType() != 1 && imp.getType() != 2) {
            IJ.error((String)"Only processed 8-bits, 16-bits, or 32 bits images.");
            return null;
        }
        if (imp.getStack().getSize() == 1) {
            IJ.error((String)"Only process stack of images.");
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

