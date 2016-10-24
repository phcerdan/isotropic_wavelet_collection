package plugins.nchenouard.isotropicwavelets;

import icy.gui.frame.IcyFrame;
import icy.gui.frame.progress.AnnounceFrame;
import icy.image.IcyBufferedImage;
import icy.main.Icy;
import icy.plugin.abstract_.PluginActionable;
import icy.sequence.Sequence;
import icy.swimmingPool.SwimmingObject;
import icy.type.collection.array.ArrayUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * Isotropic wavelet transform plugin for 2D images
 *
 * The plugin provides tools for 2D wavelet decomposition and reconstruction of
 * 2D grayscale images.
 * Transforms are as decribed in:
 *
 * Chenouard, N.; Unser, M., "3D Steerable Wavelets in Practice"
 * IEEE Transactions on Image Processing, vol.21, no.11, pp.4522,4533, Nov. 2012
 * doi: 10.1109/TIP.2012.2206044 *
 *
 * except being implemented in 2D instead of 3D.
 *
 *  Please cite the above reference in scientific communications upon use of
 * these tools.
 *
 *
 * @author Nicolas Chenouard (nicolas.chenouard.dev@gmail.com)
 * @version 1.0
 * @date 2014-05-22
 * @license gpl v3.0
 */

/**
 * Wavelet tranform GUI
 * */
public class IsotropicWavelets extends PluginActionable {
	IcyFrame mainFrame;

	JTabbedPane mainPanel;
	WaveletConfigPanel optionPanel;
	JButton processSequenceButton;
	JButton processImageButton;
	CoefficientSelectionPanel waveletResultsPanel;
	JButton displayCoefficientsButton;
	JButton reconstructButton;
	JCheckBox displayCoefficientsBox;

	Thread runningThread;
	AnnounceFrame announceFrame;

	@Override
	public void run() {
		generateGUI();
	}

	/**
	 * Initialize the GUI of the plugin
	 * */
	private void generateGUI() {
		mainFrame = new IcyFrame(
				"Isotropic wavelet transform", true, true, false, true);

		JTabbedPane mainPanel = new JTabbedPane();
		mainFrame.setContentPane(mainPanel);

		JPanel analysisPanel = new JPanel(new BorderLayout());
		mainPanel.addTab("Image Analysis", analysisPanel);

		optionPanel = new WaveletConfigPanel();
		analysisPanel.add(optionPanel, BorderLayout.CENTER);

		GridBagConstraints c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(2, 2, 2, 2);

		JPanel actionPanel = new JPanel(new GridLayout(3, 1));
		analysisPanel.add(actionPanel, BorderLayout.SOUTH);

		displayCoefficientsBox = new JCheckBox("Display coefficients", true);
		displayCoefficientsBox.setSelected(true);
		actionPanel.add(displayCoefficientsBox);
		c.gridy++;

		processImageButton = new JButton("Process the focused image");
		processImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processImage();
			}
		});
		actionPanel.add(processImageButton);

		processSequenceButton = new JButton("Process the whole sequence");
		actionPanel.add(processSequenceButton);
		processSequenceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processSequence();
			}
		});
		c.gridy++;

		JPanel reconstructionPanel = new JPanel(new BorderLayout());
		mainPanel.addTab("Image reconstruction", reconstructionPanel);

		waveletResultsPanel = new CoefficientSelectionPanel();
		reconstructionPanel.add(waveletResultsPanel, BorderLayout.CENTER);
		waveletResultsPanel.updateResultsTable();
		c.gridy++;

		JPanel coefficientsActionPanel = new JPanel(new GridBagLayout());
		reconstructionPanel.add(coefficientsActionPanel, BorderLayout.SOUTH);
		c.gridy = 0;

		displayCoefficientsButton = new JButton("Display coefficients");
		displayCoefficientsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayCurrentCoefficients();
			}
		});
		coefficientsActionPanel.add(displayCoefficientsButton, c);
		c.gridy++;

		reconstructButton =
			new JButton("Reconstruct sequence from coefficients");
		reconstructButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reconstruct();
			}
		});
		coefficientsActionPanel.add(reconstructButton, c);

		mainFrame.setPreferredSize(new Dimension(400, 300));
		mainFrame.pack();
		addIcyFrame(mainFrame);
		mainFrame.setVisible(true);
	}

	/**
	 * Display selected wavelet coefficients in the SwimmingPool as ICY images.
	 * */
	private void displayCurrentCoefficients() {
		Object selectedItem = waveletResultsPanel.resultsBox.getSelectedItem();
		if (selectedItem != null) {
			final SequenceAnalysisResults results =
				(SequenceAnalysisResults) ((SwimmingObject) selectedItem)
				.getObject();
			computationStarted();

			runningThread = new Thread() {
				@Override
				public void run() {
					displayCoefficients(results);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							computationEnded();
						}
					});
				}
			};
			runningThread.start();
		}
	}

	/**
	 * Reconstruct 2D images from selected wavelet coefficients in the
	 * SwimmingPool
	 * */
	private void reconstruct() {
		final Object selectedItem =
			waveletResultsPanel.resultsBox.getSelectedItem();
		if (selectedItem != null) {
			final SequenceAnalysisResults results =
				(SequenceAnalysisResults) ((SwimmingObject) selectedItem)
				.getObject();

			computationStarted();

			runningThread = new Thread() {
				@Override
				public void run() {
					// sort results
					ArrayList<WaveletAnalysisResults> coefficients =
						results.getAllResults();
					ArrayList<Integer> timeList =
						results.getAllAnalyzedTimesResults();
					final HashMap<WaveletAnalysisResults, Integer> mapResTime =
						new HashMap<WaveletAnalysisResults, Integer>();
					for (int cnt = 0; cnt < coefficients.size(); cnt++)
						mapResTime.put(
								coefficients.get(cnt), timeList.get(cnt));
					// create sequences
					Sequence reconstructionSequence = new Sequence();
					reconstructionSequence.setName("Reconstruction");
					Icy.getMainInterface().addSequence(reconstructionSequence);

					Sequence cropppedReconstructionSequence = null;
					if (coefficients.get(0).getPadX() > 0
							|| coefficients.get(0).getPadX() > 0) {
						cropppedReconstructionSequence = new Sequence();
						cropppedReconstructionSequence.setName(
								"Reconstruction (cropped)");
						Icy.getMainInterface().addSequence(
								cropppedReconstructionSequence);
							}
					for (int t = 0; t < coefficients.size(); t++) {
						WaveletAnalysisResults result = coefficients.get(t);
						WaveletFilterSet filterSet = result.getWaveletFilters();
						double[] reconstruction =
							IsotropicWaveletTransform
							.isotropicBandlimitedSynthesis(result);
						reconstructionSequence.addImage(
								t, new IcyBufferedImage(filterSet.getWidth(),
									filterSet.getHeight(), reconstruction));
						if (cropppedReconstructionSequence != null) {
							cropppedReconstructionSequence.addImage(
									t,
									new IcyBufferedImage(result.getFullWidth(),
										result.getFullHeight(),
										IsotropicWaveletTransform.unpadImage(
											reconstruction, result.getFullWidth(),
											result.getFullHeight(),
											filterSet.getWidth(),
											filterSet.getHeight(), result.getPadX(),
											result.getPadY())));
						}
					}
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							computationEnded();
						}
					});
				}
			};
			runningThread.start();
		}
	}

	/**
	 * Compute wavelet coefficients for all the frames of the active ICY
	 * sequence
	 * */
	private void processSequence() {
		final Sequence seq = getActiveSequence();
		if (seq != null) {
			final int width = seq.getSizeX();
			final int height = seq.getSizeY();
			final int numScales =
				optionPanel.numScaleModel.getNumber().intValue();
			final boolean prefilter =
				optionPanel.filterHighPassResidual.isSelected();
			final IsotropicWaveletType waveletType =
				(IsotropicWaveletType)
				optionPanel.waveletTypeBox.getSelectedItem();
			final boolean isotropicPadding =
				optionPanel.padForIsotropyBox.isSelected();

			computationStarted();

			runningThread = new Thread() {
				@Override
				public void run() {
					WaveletFilterSet filterSet =
						new WaveletFilterSet(waveletType, numScales, prefilter,
								width, height, isotropicPadding);
					SequenceAnalysisResults results =
						new SequenceAnalysisResults(seq.getName());
					for (int t = 0; t < seq.getSizeT(); t++) {
						IcyBufferedImage image = seq.getImage(t, 0);
						double[] data_t =
							(double[]) ArrayUtil.arrayToDoubleArray(
									image.getDataXY(0), image.isSignedDataType());
						WaveletAnalysisResults result =
							IsotropicWaveletTransform
							.isotropicBandlimitedAnalysis(data_t,
									seq.getSizeX(), seq.getSizeY(), filterSet);
						results.setResult(t, result);
					}
					Icy.getMainInterface().getSwimmingPool().add(
							new SwimmingObject(results, "Wavelet coefficients for "
								+ results.getSequenceName()));

					if (displayCoefficientsBox.isSelected()) {
						displayCoefficients(results);
					}

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							computationEnded();
						}
					});
				}
			};
			runningThread.start();
		}
	}

	/**
	 * Disable the GUI during coefficient or image computation
	 * */
	private void computationStarted() {
		processImageButton.setEnabled(false);
		processSequenceButton.setEnabled(false);
		displayCoefficientsButton.setEnabled(false);
		reconstructButton.setEnabled(false);
		announceFrame = new AnnounceFrame("Computation started");
	}

	/**
	 * Re-enable the GUI after mathematical computation has ended
	 * */
	private void computationEnded() {
		processImageButton.setEnabled(true);
		processSequenceButton.setEnabled(true);
		displayCoefficientsButton.setEnabled(true);
		reconstructButton.setEnabled(true);
		announceFrame.close();
	}

	/**
	 * Compute wavelet coefficients for the active image only
	 * */
	private void processImage() {
		final Sequence seq = getActiveSequence();
		if (seq != null) {
			final IcyBufferedImage image =
				seq.getFirstViewer().getCurrentImage();
			final int t = seq.getFirstViewer().getPositionT();

			final int width = seq.getSizeX();
			final int height = seq.getSizeY();
			final int numScales =
				optionPanel.numScaleModel.getNumber().intValue();
			final boolean prefilter =
				optionPanel.filterHighPassResidual.isSelected();
			final IsotropicWaveletType waveletType =
				(IsotropicWaveletType)
				optionPanel.waveletTypeBox.getSelectedItem();
			final boolean isotropicPadding =
				optionPanel.padForIsotropyBox.isSelected();

			computationStarted();

			runningThread = new Thread() {
				@Override
				public void run() {
					WaveletFilterSet filterSet =
						new WaveletFilterSet(waveletType, numScales, prefilter,
								width, height, isotropicPadding);

					double[] data_t = (double[]) ArrayUtil.arrayToDoubleArray(
							image.getDataXY(0), image.isSignedDataType());
					WaveletAnalysisResults result =
						IsotropicWaveletTransform.isotropicBandlimitedAnalysis(
								data_t, seq.getSizeX(), seq.getSizeY(), filterSet);
					SequenceAnalysisResults results =
						new SequenceAnalysisResults(seq.getName());
					results.setResult(t, result);
					Icy.getMainInterface().getSwimmingPool().add(
							new SwimmingObject(
								results, "Wavelet coefficients for frame of "
								+ results.getSequenceName()));

					if (displayCoefficientsBox.isSelected()) {
						displayCoefficients(results);
					}
					computationEnded();
				}
			};
			runningThread.start();
		}
	}

	/**
	 * Display some wavelet coefficients as ICY images.
	 * @param results the wavelet coefficients to display
	 * */
	public static void displayCoefficients(SequenceAnalysisResults results) {
		ArrayList<WaveletAnalysisResults> coefficients =
			results.getAllResults();
		ArrayList<Integer> timeList = results.getAllAnalyzedTimesResults();

		if (coefficients.isEmpty())
			return;
		int numScales = coefficients.get(0).getWaveletFilters().getNumScales();

		// sort results
		final HashMap<WaveletAnalysisResults, Integer> mapResTime =
			new HashMap<WaveletAnalysisResults, Integer>();
		for (int cnt = 0; cnt < coefficients.size(); cnt++)
			mapResTime.put(coefficients.get(cnt), timeList.get(cnt));
		Collections.sort(
				coefficients, new Comparator<WaveletAnalysisResults>() {
					@Override
					public int compare(
							WaveletAnalysisResults o1, WaveletAnalysisResults o2) {
						return mapResTime.get(o1).compareTo(mapResTime.get(o2));
							}
				});

		// create output sequences
		ArrayList<Sequence> waveletBandsSequenceList =
			new ArrayList<Sequence>(numScales);
		for (int scale = 0; scale < numScales; scale++) {
			Sequence scaleSeq = new Sequence();
			scaleSeq.setName("scale " + scale);
			waveletBandsSequenceList.add(scaleSeq);
			Icy.getMainInterface().addSequence(scaleSeq);
		}
		Sequence lpSequence = new Sequence();
		lpSequence.setName("Low frequency residual");
		Icy.getMainInterface().addSequence(lpSequence);

		Sequence hpSequence = null;
		if (coefficients.get(0).getHPResidual() != null) {
			hpSequence = new Sequence();
			hpSequence.setName("High frequency residual");
			Icy.getMainInterface().addSequence(hpSequence);
		}
		for (int t = 0; t < coefficients.size(); t++) {
			WaveletAnalysisResults result = coefficients.get(t);
			WaveletFilterSet filterSet = result.getWaveletFilters();
			for (int scale = 0; scale < numScales; scale++) {
				IcyBufferedImage scaleImage =
					new IcyBufferedImage(filterSet.getScaleWidth(scale),
							result.getWaveletFilters().getScaleHeight(scale),
							result.getWaveletBand(scale));
				Sequence scaleSeq = waveletBandsSequenceList.get(scale);
				scaleSeq.addImage(t, scaleImage);
			}
			IcyBufferedImage lpImage =
				new IcyBufferedImage(filterSet.getLPWidth(),
						filterSet.getLPHeight(), result.getLPResidual());
			lpSequence.addImage(t, lpImage);
			if (hpSequence != null) {
				IcyBufferedImage hpImage =
					new IcyBufferedImage(filterSet.getHPWidth(),
							filterSet.getHPHeight(), result.getHPResidual());
				hpSequence.addImage(t, hpImage);
			}
		}
	}
}
