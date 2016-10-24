package plugins.nchenouard.isotropicwavelets;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class WaveletConfigPanel extends JPanel {
	/**
	 * GUI for manually setting wavelet decomposition parameters
	 *
	 * @author Nicolas Chenouard (nicolas.chenouard.dev@gmail.com)
	 * @version 1.0
	 * @date 2014-05-22
	 * @license gpl v3.0
	 */

	private static final long serialVersionUID = -7253036809006307841L;
	JComboBox<IsotropicWaveletType> waveletTypeBox;
	JSpinner numScaleSpinner;
	SpinnerNumberModel numScaleModel;
	JCheckBox filterHighPassResidual;
	JCheckBox padForIsotropyBox;

	public WaveletConfigPanel() {
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(2, 2, 2, 2);

		this.add(new JLabel("Number of scales:"), c);
		c.gridy++;

		numScaleModel = new SpinnerNumberModel(3, 1, 100, 1);
		numScaleSpinner = new JSpinner(numScaleModel);
		this.add(numScaleSpinner, c);
		c.gridy++;

		this.add(new JLabel("Wavelet profile:"), c);
		c.gridy++;

		waveletTypeBox =
			new JComboBox<IsotropicWaveletType>(IsotropicWaveletType.values());
		this.add(waveletTypeBox, c);
		c.gridy++;

		filterHighPassResidual =
			new JCheckBox("Filter out high frequencies", true);
		this.add(filterHighPassResidual, c);
		c.gridy++;

		padForIsotropyBox =
			new JCheckBox("Isotropic zero-padding of image", false);
		this.add(padForIsotropyBox, c);
		c.gridy++;
	}

	/**
	 * Get the number of scales to use for wavelet decomposition
	 * @return number of wavelet scales
	 * */
	public int getNumScales() {
		return numScaleModel.getNumber().intValue();
	}

	/**
	 * Determine whether a prefilter needs to be used to eliminate the high
	 * frequency residual
	 * @return true if prefiltering of high frequencies has to be performed
	 * */
	public boolean isPrefilter() {
		return filterHighPassResidual.isSelected();
	}

	/**
	 * Get the type of radial function to use for wavelet decomposition
	 * @return type of radial wavelet function
	 * */
	public IsotropicWaveletType getWaveletType() {
		return (IsotropicWaveletType) waveletTypeBox.getSelectedItem();
	}

	/**
	 * Determine whether zero-padding is to be used to make the analyzed image
	 * isotropic
	 * @return true if zero padding is required
	 * */
	public boolean isIsotropicPadding() {
		return padForIsotropyBox.isSelected();
	}
}
