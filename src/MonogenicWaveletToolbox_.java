/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  ij.ImagePlus
 *  ij.WindowManager
 *  ij.gui.MessageDialog
 *  ij.plugin.frame.PlugInFrame
 *  ij.process.ColorProcessor
 *  ij.process.FloatProcessor
 *  ij.process.ImageProcessor
 */
package monogenicwavelettoolbox;

import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.MessageDialog;
import ij.plugin.frame.PlugInFrame;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import monogenicwavelettoolbox.About_;
import monogenicwavelettoolbox.FloatArrayGeneric;
import monogenicwavelettoolbox.ImageAdjuster;
import monogenicwavelettoolbox.MonogenicDecompositionTree;
import monogenicwavelettoolbox.MonogenicDecompositionTree2D;
import monogenicwavelettoolbox.MonogenicDecompositionTree3D;
import monogenicwavelettoolbox.PropertiesEnum;
import monogenicwavelettoolbox.ReconstructionEnum;

public class MonogenicWaveletToolbox_
extends PlugInFrame {
    private static final long serialVersionUID = 6627226984150029355L;
    protected ImagePlus mWorkingImage;
    protected MonogenicDecompositionTree mMonogenicDecompositionTree;
    protected JTextField mNumberOfOctavesField;
    protected JTextField mNumberOfChannelsField;
    protected JTextField mOrderOfWaveletsField;
    protected JRadioButton mShowOriginalRadioButton = new JRadioButton(PropertiesEnum.ORIGINAL.getName());
    protected JRadioButton mShowR1RadioButton = new JRadioButton(PropertiesEnum.R1.getName());
    protected JRadioButton mShowR2RadioButton = new JRadioButton(PropertiesEnum.R2.getName());
    protected JRadioButton mShowR3RadioButton = new JRadioButton(PropertiesEnum.R3.getName());
    protected JRadioButton mShowAmplitudeRadioButton = new JRadioButton(PropertiesEnum.AMPLITUDE.getName());
    protected JRadioButton mShowPhaseRadioButton = new JRadioButton(PropertiesEnum.PHASE.getName());
    protected JRadioButton mShowCosPhaseRadioButton = new JRadioButton(PropertiesEnum.COS_PHASE.getName());
    protected JRadioButton mShowAttenuationRadioButton = new JRadioButton(PropertiesEnum.ATTENUATION.getName());
    protected JRadioButton mShowOrientationRadioButton = new JRadioButton(PropertiesEnum.ORIENTATION.getName());
    protected JRadioButton mShowFrequencyRadioButton = new JRadioButton(PropertiesEnum.FREQUENCY.getName());
    protected JRadioButton mAutomaticIlluminationAdjustRadioButton = new JRadioButton("Equalize Illumination (also for 2D color images)");
    protected JRadioButton mAutomaticIlluminationAdjustStabilizedRadioButton = new JRadioButton("Equalize Illumination (stablized)");
    protected JRadioButton mAutomaticDescreeningRadioButton = new JRadioButton("Descreen");
    protected JRadioButton mAutomaticDenoisingRadioButton = new JRadioButton("Denoising (set to 32-bit and scale to [0-1] first)");
    protected JRadioButton mAutomaticStructureRemovalRadioButton = new JRadioButton("Remove dominant structure");
    protected JRadioButton mAutomaticStructureRemovalAdaptiveRadioButton = new JRadioButton("Remove dominant structure (Adaptive)");
    protected JRadioButton mLosslessRadioButton = new JRadioButton(ReconstructionEnum.LOSSLESS.getName());
    protected JRadioButton mAmpOnlyRadioButton = new JRadioButton(ReconstructionEnum.AMP_ONLY.getName());
    protected JRadioButton mPhaseOnlyRadioButton = new JRadioButton(ReconstructionEnum.PHASE_ONLY.getName());
    protected JRadioButton mAmpPhaseRadioButton = new JRadioButton(ReconstructionEnum.AMP_PHASE.getName());
    protected JRadioButton mWaveRemovalRadioButton = new JRadioButton(ReconstructionEnum.WAVE_REMOVAL.getName());
    protected JRadioButton mWaveRemovalAdaptiveRadioButton = new JRadioButton(ReconstructionEnum.WAVE_REMOVAL_ADAPTIVE.getName());
    protected JRadioButton mDescreenRadioButton = new JRadioButton(ReconstructionEnum.DESCREENING.getName());
    protected JRadioButton mPhaseStabRadioButton = new JRadioButton(ReconstructionEnum.PHASE_ONLY_STAB.getName());
    protected JRadioButton mDenoiseRadioButton = new JRadioButton(ReconstructionEnum.DENOISE.getName());
    protected ButtonGroup propertySelectionButtonGroup = new ButtonGroup();
    protected JSlider mOctaveSelectionSlider;
    protected JSlider mChannelSelectionSlider;
    protected int mNumberOfChannels = 1;
    protected int mNumberOfOctaves = 3;
    protected int mOrderOfWavelets = 5;
    protected JPanel mPropertySelectionPanel;
    protected JCheckBox mSymmetricExtensionCheckBox = new JCheckBox("Symmetric Extension \n(only 2D)");
    protected JCheckBox mShowItemsInFullSizeCheckBox = new JCheckBox("Show in full size");
    protected JTextField mReconstructionThresholdTextField = new JTextField("10.0");
    protected boolean mBuildGui;
    private JPanel mMainPanel;
    private JPanel mMainPanelManual;

    public MonogenicWaveletToolbox_() {
        super("Monogenic Wavelet Toolbox");
        this.mBuildGui = true;
        this.buildGUI();
    }

    public MonogenicWaveletToolbox_(boolean buildGui) {
        super("Monogenic Wavelet Toolbox");
        this.mBuildGui = buildGui;
        if (buildGui) {
            this.buildGUI();
        }
    }

    private void buildGUI() {
        this.mMainPanel = new JPanel();
        this.mMainPanelManual = new JPanel(new BorderLayout());
        JPanel mainPanelAutomatic = new JPanel(new BorderLayout());
        JButton automaticButton = new JButton("Execute");
        automaticButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                long startTime = System.currentTimeMillis();
                MonogenicWaveletToolbox_.this.acquireImage();
                if (MonogenicWaveletToolbox_.this.mWorkingImage == null) {
                    return;
                }
                try {
                    if (MonogenicWaveletToolbox_.this.mAutomaticIlluminationAdjustRadioButton.isSelected() || MonogenicWaveletToolbox_.this.mAutomaticIlluminationAdjustStabilizedRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.performEqualizationOfBrightness(MonogenicWaveletToolbox_.this.mWorkingImage, MonogenicWaveletToolbox_.this.mAutomaticIlluminationAdjustStabilizedRadioButton.isSelected(), false);
                    } else if (MonogenicWaveletToolbox_.this.mAutomaticDescreeningRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.performDescreen(MonogenicWaveletToolbox_.this.mWorkingImage);
                    } else if (MonogenicWaveletToolbox_.this.mAutomaticStructureRemovalRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mNumberOfChannels = 1;
                        MonogenicWaveletToolbox_.this.mNumberOfOctaves = ImageAdjuster.getMaximalNumberOfOctaves(MonogenicWaveletToolbox_.this.mWorkingImage);
                        MonogenicWaveletToolbox_.this.mOrderOfWavelets = 1;
                        MonogenicWaveletToolbox_.this.startDecomposition();
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.WAVE_REMOVAL);
                    } else if (MonogenicWaveletToolbox_.this.mAutomaticStructureRemovalAdaptiveRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mNumberOfChannels = 1;
                        MonogenicWaveletToolbox_.this.mNumberOfOctaves = ImageAdjuster.getMaximalNumberOfOctaves(MonogenicWaveletToolbox_.this.mWorkingImage);
                        MonogenicWaveletToolbox_.this.mOrderOfWavelets = 1;
                        MonogenicWaveletToolbox_.this.startDecomposition();
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.WAVE_REMOVAL_ADAPTIVE);
                    } else if (MonogenicWaveletToolbox_.this.mAutomaticDenoisingRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mNumberOfChannels = 5;
                        MonogenicWaveletToolbox_.this.mNumberOfOctaves = ImageAdjuster.getMaximalNumberOfOctaves(MonogenicWaveletToolbox_.this.mWorkingImage);
                        MonogenicWaveletToolbox_.this.mOrderOfWavelets = 1;
                        MonogenicWaveletToolbox_.this.startDecomposition();
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.DENOISE);
                    }
                }
                catch (OutOfMemoryError e1) {
                    new ij.gui.MessageDialog((Frame)((Object)MonogenicWaveletToolbox_.this), "Error", "Out of memory. Try a smaller image.");
                    return;
                }
                System.out.println("Total elapsed time: " + (System.currentTimeMillis() - startTime) + " ms");
            }
        });
        mainPanelAutomatic.add((Component)automaticButton, "South");
        JPanel automaticRadioButtonPanel = new JPanel(new GridLayout(4, 1));
        mainPanelAutomatic.add((Component)automaticRadioButtonPanel, "North");
        automaticRadioButtonPanel.add(this.mAutomaticDescreeningRadioButton);
        automaticRadioButtonPanel.add(this.mAutomaticIlluminationAdjustRadioButton);
        automaticRadioButtonPanel.add(this.mAutomaticIlluminationAdjustStabilizedRadioButton);
        automaticRadioButtonPanel.add(this.mAutomaticDenoisingRadioButton);
        ButtonGroup automaticPanelButtonGroup = new ButtonGroup();
        automaticPanelButtonGroup.add(this.mAutomaticDescreeningRadioButton);
        automaticPanelButtonGroup.add(this.mAutomaticIlluminationAdjustRadioButton);
        automaticPanelButtonGroup.add(this.mAutomaticIlluminationAdjustStabilizedRadioButton);
        automaticPanelButtonGroup.add(this.mAutomaticStructureRemovalAdaptiveRadioButton);
        automaticPanelButtonGroup.add(this.mAutomaticStructureRemovalRadioButton);
        automaticPanelButtonGroup.add(this.mAutomaticDenoisingRadioButton);
        this.mMainPanel.add("Manual Decomp./Rec.", this.mMainPanelManual);
        this.add((Component)this.mMainPanel);
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Info");
        MenuItem aboutMenuItem = new MenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                About_.showAbout();
            }
        });
        this.mShowOriginalRadioButton.setSelected(true);
        menu.add(aboutMenuItem);
        this.setMenuBar(menuBar);
        JPanel decompositionPanel = new JPanel(new BorderLayout());
        decompositionPanel.setBorder(BorderFactory.createTitledBorder("Decomposition"));
        JPanel decompositionTextPanel = new JPanel(new GridLayout(2, 2));
        this.mNumberOfOctavesField = new JTextField("3");
        this.mNumberOfOctavesField.setBorder(BorderFactory.createTitledBorder("No. Octaves"));
        this.mNumberOfChannelsField = new JTextField("1");
        this.mNumberOfChannelsField.setBorder(BorderFactory.createTitledBorder("No. Voices"));
        this.mOrderOfWaveletsField = new JTextField("1");
        this.mOrderOfWaveletsField.setBorder(BorderFactory.createTitledBorder("Order of Wavelets"));
        decompositionTextPanel.add(this.mNumberOfOctavesField);
        decompositionTextPanel.add(this.mNumberOfChannelsField);
        decompositionTextPanel.add(this.mOrderOfWaveletsField);
        decompositionTextPanel.add(this.mSymmetricExtensionCheckBox);
        decompositionPanel.add((Component)decompositionTextPanel, "Center");
        JButton computeDecompositionButton = new JButton("Decomposition");
        computeDecompositionButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                MonogenicWaveletToolbox_.this.acquireImage();
                if (MonogenicWaveletToolbox_.this.mWorkingImage == null) {
                    return;
                }
                try {
                    MonogenicWaveletToolbox_.this.mNumberOfOctaves = Integer.parseInt(MonogenicWaveletToolbox_.this.mNumberOfOctavesField.getText());
                    MonogenicWaveletToolbox_.this.mNumberOfChannels = Integer.parseInt(MonogenicWaveletToolbox_.this.mNumberOfChannelsField.getText());
                    MonogenicWaveletToolbox_.this.mOrderOfWavelets = Integer.parseInt(MonogenicWaveletToolbox_.this.mOrderOfWaveletsField.getText());
                    if (MonogenicWaveletToolbox_.this.mNumberOfOctaves > ImageAdjuster.getMaximalNumberOfOctaves(MonogenicWaveletToolbox_.this.mWorkingImage)) {
                        new ij.gui.MessageDialog((Frame)((Object)MonogenicWaveletToolbox_.this), "Error", "Maximum number of octaves for this image is " + ImageAdjuster.getMaximalNumberOfOctaves(MonogenicWaveletToolbox_.this.mWorkingImage));
                        return;
                    }
                    if (MonogenicWaveletToolbox_.this.mOrderOfWavelets > 5 || MonogenicWaveletToolbox_.this.mOrderOfWavelets < 0) {
                        new ij.gui.MessageDialog((Frame)((Object)MonogenicWaveletToolbox_.this), "Error", "Choose smoothness order of wavelets between 0 and 5");
                        return;
                    }
                    MonogenicWaveletToolbox_.this.startDecomposition();
                }
                catch (OutOfMemoryError e1) {
                    new ij.gui.MessageDialog((Frame)((Object)MonogenicWaveletToolbox_.this), "Error", "Out of memory. Try a smaller image.");
                    return;
                }
            }
        });
        decompositionPanel.add((Component)computeDecompositionButton, "South");
        this.mMainPanelManual.add((Component)decompositionPanel, "North");
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createTitledBorder("Display"));
        this.mPropertySelectionPanel = new JPanel(new GridLayout(4, 2));
        this.propertySelectionButtonGroup.add(this.mShowOriginalRadioButton);
        this.propertySelectionButtonGroup.add(this.mShowR1RadioButton);
        this.propertySelectionButtonGroup.add(this.mShowR2RadioButton);
        this.propertySelectionButtonGroup.add(this.mShowR3RadioButton);
        this.propertySelectionButtonGroup.add(this.mShowAmplitudeRadioButton);
        this.propertySelectionButtonGroup.add(this.mShowOrientationRadioButton);
        this.propertySelectionButtonGroup.add(this.mShowPhaseRadioButton);
        this.propertySelectionButtonGroup.add(this.mShowCosPhaseRadioButton);
        this.propertySelectionButtonGroup.add(this.mShowFrequencyRadioButton);
        JPanel detailSelectionParentPanel = new JPanel(new BorderLayout());
        JPanel detailSelectionPanel = new JPanel(new GridLayout(1, 2));
        detailSelectionParentPanel.add((Component)detailSelectionPanel, "Center");
        detailSelectionParentPanel.add((Component)this.mShowItemsInFullSizeCheckBox, "South");
        displayPanel.add((Component)detailSelectionParentPanel, "North");
        this.mOctaveSelectionSlider = new JSlider(0, 0, this.mNumberOfOctaves + 1, 1);
        detailSelectionPanel.add(this.mOctaveSelectionSlider);
        this.mOctaveSelectionSlider.setBorder(BorderFactory.createTitledBorder("Octave"));
        this.mOctaveSelectionSlider.setMajorTickSpacing(1);
        this.mOctaveSelectionSlider.setPaintTicks(true);
        this.mOctaveSelectionSlider.setPaintLabels(true);
        this.mChannelSelectionSlider = new JSlider(0, 1, this.mNumberOfChannels, 1);
        detailSelectionPanel.add(this.mChannelSelectionSlider);
        this.mChannelSelectionSlider.setBorder(BorderFactory.createTitledBorder("Voice"));
        this.mChannelSelectionSlider.setMajorTickSpacing(1);
        this.mChannelSelectionSlider.setPaintTicks(true);
        this.mChannelSelectionSlider.setPaintLabels(true);
        JButton displayButton = new JButton("Display");
        displayPanel.add((Component)displayButton, "South");
        displayButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                if (MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree == null) {
                    new ij.gui.MessageDialog((Frame)((Object)MonogenicWaveletToolbox_.this), "Error", "Decompose first the image");
                    return;
                }
                try {
                    int selectedOctave = MonogenicWaveletToolbox_.this.mOctaveSelectionSlider.getValue();
                    int selectedChannel = MonogenicWaveletToolbox_.this.mChannelSelectionSlider.getValue();
                    if (MonogenicWaveletToolbox_.this.mShowOriginalRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showDecompositionItem(selectedOctave, selectedChannel, PropertiesEnum.ORIGINAL, MonogenicWaveletToolbox_.this.mShowItemsInFullSizeCheckBox.isSelected());
                    } else if (MonogenicWaveletToolbox_.this.mShowR1RadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showDecompositionItem(selectedOctave, selectedChannel, PropertiesEnum.R1, MonogenicWaveletToolbox_.this.mShowItemsInFullSizeCheckBox.isSelected());
                    } else if (MonogenicWaveletToolbox_.this.mShowR2RadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showDecompositionItem(selectedOctave, selectedChannel, PropertiesEnum.R2, MonogenicWaveletToolbox_.this.mShowItemsInFullSizeCheckBox.isSelected());
                    } else if (MonogenicWaveletToolbox_.this.mShowR3RadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showDecompositionItem(selectedOctave, selectedChannel, PropertiesEnum.R3, MonogenicWaveletToolbox_.this.mShowItemsInFullSizeCheckBox.isSelected());
                    } else if (MonogenicWaveletToolbox_.this.mShowAmplitudeRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showDecompositionItem(selectedOctave, selectedChannel, PropertiesEnum.AMPLITUDE, MonogenicWaveletToolbox_.this.mShowItemsInFullSizeCheckBox.isSelected());
                    } else if (MonogenicWaveletToolbox_.this.mShowAttenuationRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showDecompositionItem(selectedOctave, selectedChannel, PropertiesEnum.ATTENUATION, MonogenicWaveletToolbox_.this.mShowItemsInFullSizeCheckBox.isSelected());
                    } else if (MonogenicWaveletToolbox_.this.mShowPhaseRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showDecompositionItem(selectedOctave, selectedChannel, PropertiesEnum.PHASE, MonogenicWaveletToolbox_.this.mShowItemsInFullSizeCheckBox.isSelected());
                    } else if (MonogenicWaveletToolbox_.this.mShowCosPhaseRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showDecompositionItem(selectedOctave, selectedChannel, PropertiesEnum.COS_PHASE, MonogenicWaveletToolbox_.this.mShowItemsInFullSizeCheckBox.isSelected());
                    } else if (MonogenicWaveletToolbox_.this.mShowOrientationRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showDecompositionItem(selectedOctave, selectedChannel, PropertiesEnum.ORIENTATION, MonogenicWaveletToolbox_.this.mShowItemsInFullSizeCheckBox.isSelected());
                    } else if (MonogenicWaveletToolbox_.this.mShowFrequencyRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showDecompositionItem(selectedOctave, selectedChannel, PropertiesEnum.FREQUENCY, MonogenicWaveletToolbox_.this.mShowItemsInFullSizeCheckBox.isSelected());
                    }
                }
                catch (OutOfMemoryError e1) {
                    new ij.gui.MessageDialog((Frame)((Object)MonogenicWaveletToolbox_.this), "Error", "Out of memory. Try a smaller image or ImageJ increase memory size in Edit->Options->Memory & Threads.");
                    return;
                }
            }
        });
        JPanel reconstructionPanel = new JPanel(new BorderLayout());
        reconstructionPanel.setBorder(BorderFactory.createTitledBorder("Reconstruction"));
        JPanel reconstructionRadioPanel = new JPanel(new GridLayout(3, 2));
        ButtonGroup reconstructionMethodButtonGroup = new ButtonGroup();
        reconstructionMethodButtonGroup.add(this.mLosslessRadioButton);
        reconstructionMethodButtonGroup.add(this.mPhaseOnlyRadioButton);
        reconstructionMethodButtonGroup.add(this.mAmpOnlyRadioButton);
        reconstructionMethodButtonGroup.add(this.mAmpPhaseRadioButton);
        reconstructionMethodButtonGroup.add(this.mWaveRemovalRadioButton);
        this.mLosslessRadioButton.setSelected(true);
        reconstructionMethodButtonGroup.add(this.mWaveRemovalAdaptiveRadioButton);
        reconstructionMethodButtonGroup.add(this.mDescreenRadioButton);
        reconstructionMethodButtonGroup.add(this.mPhaseStabRadioButton);
        reconstructionMethodButtonGroup.add(this.mDenoiseRadioButton);
        reconstructionRadioPanel.add(this.mLosslessRadioButton);
        reconstructionRadioPanel.add(this.mPhaseOnlyRadioButton);
        reconstructionRadioPanel.add(this.mAmpOnlyRadioButton);
        reconstructionRadioPanel.add(this.mDescreenRadioButton);
        reconstructionRadioPanel.add(this.mPhaseStabRadioButton);
        JPanel thresholdPanel = new JPanel(new BorderLayout());
        thresholdPanel.setBorder(BorderFactory.createTitledBorder("Treshhold"));
        thresholdPanel.add((Component)this.mReconstructionThresholdTextField, "Center");
        reconstructionPanel.add((Component)reconstructionRadioPanel, "Center");
        JButton reconstructionButton = new JButton("Reconstruction");
        reconstructionButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                if (MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree == null) {
                    new ij.gui.MessageDialog((Frame)((Object)MonogenicWaveletToolbox_.this), "Error", "Decompose Image First");
                    return;
                }
                try {
                    if (MonogenicWaveletToolbox_.this.mLosslessRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.LOSSLESS);
                    } else if (MonogenicWaveletToolbox_.this.mAmpOnlyRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.AMP_ONLY);
                    } else if (MonogenicWaveletToolbox_.this.mPhaseOnlyRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.PHASE_ONLY);
                    } else if (MonogenicWaveletToolbox_.this.mAmpPhaseRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.AMP_PHASE);
                    } else if (MonogenicWaveletToolbox_.this.mWaveRemovalRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.WAVE_REMOVAL);
                    } else if (MonogenicWaveletToolbox_.this.mWaveRemovalAdaptiveRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.WAVE_REMOVAL_ADAPTIVE);
                    } else if (MonogenicWaveletToolbox_.this.mDescreenRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.DESCREENING);
                    } else if (MonogenicWaveletToolbox_.this.mPhaseStabRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.PHASE_ONLY_STAB, Double.parseDouble(MonogenicWaveletToolbox_.this.mReconstructionThresholdTextField.getText()));
                    } else if (MonogenicWaveletToolbox_.this.mDenoiseRadioButton.isSelected()) {
                        MonogenicWaveletToolbox_.this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.DENOISE, Double.parseDouble(MonogenicWaveletToolbox_.this.mReconstructionThresholdTextField.getText()));
                    }
                }
                catch (OutOfMemoryError e1) {
                    new ij.gui.MessageDialog((Frame)((Object)MonogenicWaveletToolbox_.this), "Error", "Not enough memory. Try a smaller image or increase ImageJ memory by Edit -> Options -> Memory and Threads.");
                    return;
                }
            }
        });
        reconstructionPanel.add((Component)reconstructionButton, "South");
        this.mMainPanelManual.add((Component)displayPanel, "Center");
        this.mMainPanelManual.add((Component)reconstructionPanel, "South");
        this.mShowAmplitudeRadioButton.setToolTipText("The amplitude is the absolute value of the hypercomplex wavelet coeffients.");
        this.mShowPhaseRadioButton.setToolTipText("The phase is the arcus cosine of of the real part and the amplitude.");
        this.mShowCosPhaseRadioButton.setToolTipText("The cosine of the phase.");
        this.mShowOrientationRadioButton.setToolTipText("The phase orientation is the arcus tangens of the imaginary parts 1 and 2.");
        this.mShowFrequencyRadioButton.setToolTipText("The frequency is the directional derivative of the phase into the phase orientation.");
        this.mShowItemsInFullSizeCheckBox.setToolTipText("Check this box to see coefficient images in full size.");
        this.mSymmetricExtensionCheckBox.setToolTipText("Chech this box to symetrically extend the image before decomposition. This may reduce Gibbs-like artifacts but increases runtime.");
        this.mOrderOfWaveletsField.setToolTipText("The order of smoothness of the wavelet used. Choose between 0 and 5.");
        this.mNumberOfOctavesField.setToolTipText("The number of octaves used for the decomposition.");
        this.mNumberOfChannelsField.setToolTipText("The number of voices per octaves.");
        this.mOctaveSelectionSlider.setToolTipText("Select the octave to display. 0: original image, 1 to (No. Octaves): the corresponding octave subband,(No. Octave) + 1: the low pass component.");
        this.mChannelSelectionSlider.setToolTipText("Select the voice of the current octave. Obsolete for Octave = 0 and Octave = (No. Octaves) + 1.");
        this.mShowOriginalRadioButton.setToolTipText("The real part of the transform coefficients.");
        this.mShowR1RadioButton.setToolTipText("The first imaginary part of the transform coefficients.");
        this.mShowR2RadioButton.setToolTipText("The second imaginary part of the transform coefficients.");
        this.mShowR3RadioButton.setToolTipText("The third imaginary part of the transform coefficients.");
        this.mLosslessRadioButton.setToolTipText("Perfect reconstruction of the wavelet coefficients.");
        this.mDescreenRadioButton.setToolTipText("Removal of the screening effect from images, e.g. newspaper images.");
        this.mPhaseOnlyRadioButton.setToolTipText("Reconstruction from cosine of the coefficients phase only, setting amplitude to 1.");
        this.mPhaseStabRadioButton.setToolTipText("Noise suppressed reconstruction from cosine of the coefficients phase only, setting amplitude to 1.");
        this.mAmpOnlyRadioButton.setToolTipText("Reconstruction from the coefficients amplitude only, setting phase to 0.");
        displayPanel.add((Component)this.mPropertySelectionPanel, "Center");
        this.updateGUI(false);
        this.pack();
        this.setVisible(true);
    }

    private void updateGUI(boolean is3d) {
        if (is3d) {
            this.mPropertySelectionPanel.removeAll();
            this.mPropertySelectionPanel.add(this.mShowOriginalRadioButton);
            this.mPropertySelectionPanel.add(this.mShowAmplitudeRadioButton);
            this.mPropertySelectionPanel.add(this.mShowR1RadioButton);
            this.mPropertySelectionPanel.add(this.mShowPhaseRadioButton);
            this.mPropertySelectionPanel.add(this.mShowR2RadioButton);
            this.mPropertySelectionPanel.add(this.mShowCosPhaseRadioButton);
            this.mPropertySelectionPanel.add(this.mShowR3RadioButton);
            this.mPropertySelectionPanel.add(this.mShowFrequencyRadioButton);
        } else {
            this.mPropertySelectionPanel.removeAll();
            this.mPropertySelectionPanel.add(this.mShowOriginalRadioButton);
            this.mPropertySelectionPanel.add(this.mShowAmplitudeRadioButton);
            this.mPropertySelectionPanel.add(this.mShowR1RadioButton);
            this.mPropertySelectionPanel.add(this.mShowPhaseRadioButton);
            this.mPropertySelectionPanel.add(this.mShowR2RadioButton);
            this.mPropertySelectionPanel.add(this.mShowCosPhaseRadioButton);
            this.mPropertySelectionPanel.add(this.mShowOrientationRadioButton);
            this.mPropertySelectionPanel.add(this.mShowFrequencyRadioButton);
        }
        this.mOctaveSelectionSlider.setMaximum(this.mNumberOfOctaves + 1);
        this.mChannelSelectionSlider.setMaximum(this.mNumberOfChannels);
        this.mNumberOfOctavesField.setText("" + this.mNumberOfOctaves);
        this.mNumberOfChannelsField.setText("" + this.mNumberOfChannels);
        this.mOrderOfWaveletsField.setText("" + this.mOrderOfWavelets);
        this.pack();
    }

    protected void startDecomposition() {
        this.startDecomposition(this.mWorkingImage);
    }

    protected boolean startDecomposition(ImagePlus imp) {
        if (imp.getStackSize() == 1) {
            this.mMonogenicDecompositionTree = new MonogenicDecompositionTree2D(imp, this.mNumberOfOctaves, this.mNumberOfChannels, this.mOrderOfWavelets, this.mSymmetricExtensionCheckBox.isSelected());
            this.updateGUI(false);
            return true;
        }
        if (!ImageAdjuster.checkDimension(imp)) {
            new ij.gui.MessageDialog((Frame)((Object)this), "Error", "For performance reasons the size of 3D images must be a power of 2 in each dimension (width, height and stacksize)");
            return false;
        }
        this.mMonogenicDecompositionTree = new MonogenicDecompositionTree3D(imp, this.mNumberOfOctaves, this.mNumberOfChannels, this.mOrderOfWavelets);
        this.updateGUI(true);
        return true;
    }

    protected void acquireImage() {
        this.mWorkingImage = WindowManager.getCurrentImage();
        if (this.mWorkingImage == null) {
            new ij.gui.MessageDialog((Frame)((Object)this), "Error", "No Image Loaded");
            return;
        }
        this.mWorkingImage.updateImage();
    }

    public void performEqualizationOfBrightness(ImagePlus imp, boolean stabilized, boolean contrastEnhance) {
        ImageProcessor ip = imp.getProcessor();
        this.mNumberOfChannels = 5;
        this.mNumberOfOctaves = ImageAdjuster.getMaximalNumberOfOctaves(imp);
        this.mOrderOfWavelets = 1;
        if (ip instanceof ColorProcessor) {
            if (imp.getStackSize() > 1) {
                new ij.gui.MessageDialog((Frame)((Object)this), "Error", "Illumination equalization does not work for stacks of coloured images");
                return;
            }
            ColorProcessor ipc = (ColorProcessor)ip;
            FloatProcessor[] lab = ImageAdjuster.RGBtoLAB(ipc);
            if (!this.startDecomposition(new ImagePlus("", (ImageProcessor)lab[0]))) {
                return;
            }
            FloatArrayGeneric floatArr = null;
            floatArr = stabilized ? this.mMonogenicDecompositionTree.getReconstruction(ReconstructionEnum.PHASE_ONLY_STAB, 0.0, !contrastEnhance) : this.mMonogenicDecompositionTree.getReconstruction(ReconstructionEnum.PHASE_ONLY, 0.0, !contrastEnhance);
            if (contrastEnhance) {
                floatArr.scaleTo(0.0, 1.0).add(this.mMonogenicDecompositionTree.getImage().add(this.mMonogenicDecompositionTree.getMeanValue()).scaleTo(0.0, 1.0));
            }
            ImagePlus rec = floatArr.getImagePlusReal("", 1, ipc.getHeight(), ipc.getWidth());
            lab[0] = rec.getProcessor();
            ImageAdjuster.scaleArray((float[])lab[0].getPixels(), 0.0f, 100.0f);
            ColorProcessor ipcRec = ImageAdjuster.LABtoRGB((ImageProcessor[])lab);
            ImagePlus reconstructed = new ImagePlus(String.valueOf(imp.getTitle()) + ", Equalization of Brightness", (ImageProcessor)ipcRec);
            reconstructed.updateAndDraw();
            reconstructed.resetDisplayRange();
            reconstructed.updateAndDraw();
            reconstructed.show();
        } else {
            if (!this.startDecomposition(imp)) {
                return;
            }
            FloatArrayGeneric rec = null;
            String title = String.valueOf(imp.getTitle()) + "Equalization of Brightness";
            if (stabilized) {
                rec = this.mMonogenicDecompositionTree.getReconstruction(ReconstructionEnum.PHASE_ONLY_STAB, 0.0, !contrastEnhance);
                title = String.valueOf(title) + " (noise suppressed)";
            } else {
                rec = this.mMonogenicDecompositionTree.getReconstruction(ReconstructionEnum.PHASE_ONLY, 0.0, !contrastEnhance);
            }
            if (contrastEnhance) {
                rec.scaleTo(0.0, 0.5);
                rec.add(this.mMonogenicDecompositionTree.getImage().scaleTo(0.0, 0.5));
            }
            ImagePlus reconstructed = rec.getImagePlusReal(title, imp.getStackSize(), imp.getHeight(), imp.getWidth());
            reconstructed.updateAndDraw();
            reconstructed.show();
        }
    }

    public void performDescreen(ImagePlus imp) {
        this.mNumberOfChannels = 1;
        this.mNumberOfOctaves = ImageAdjuster.getMaximalNumberOfOctaves(imp);
        this.mOrderOfWavelets = 1;
        if (!this.startDecomposition(imp)) {
            return;
        }
        this.mMonogenicDecompositionTree.showReconstruction(ReconstructionEnum.DESCREENING);
    }

}

