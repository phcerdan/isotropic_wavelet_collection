package plugins.nchenouard.isotropicwavelets;

import java.util.ArrayList;

/**
 * Set of wavelet coefficients for a temporal sequence of 2D images
 *
 * @author Nicolas Chenouard (nicolas.chenouard.dev@gmail.com)
 * @version 1.0
 * @date 2014-05-22
 * @license gpl v3.0
 */

public class SequenceAnalysisResults {
	protected String sequenceName = ""; // name of the analyzed sequence
	protected ArrayList<Integer> analyzedTimes =
		new ArrayList<Integer>(); // set of frames that have been analyzed with
	// the wavelet transform
	protected ArrayList<WaveletAnalysisResults> results =
		new ArrayList<WaveletAnalysisResults>(); // set of wavelet coefficients
	// corresponding to the
	// analyzed frames

	/**
	 * Initialize the results objects using a defined sequence name
	 * @param sequenceName the name of the analyzed sequence
	 * */
	public SequenceAnalysisResults(String sequenceName) {
		this.sequenceName = sequenceName;
	}
	/**
	 * Get the name of the analyzed sequence
	 * @return name of the analyzed sequence
	 * */
	public String getSequenceName() {
		return sequenceName;
	}

	/**
	 * Get the set of wavelet coefficients for all the analyzed frames with the
	 * wavelet transform
	 * @return array of wavelet coefficients corresponding to analyzed frames
	 * */
	public ArrayList<WaveletAnalysisResults> getAllResults() {
		return new ArrayList<WaveletAnalysisResults>(results);
	}

	/**
	 * Get the time index of analyzed frames with the wavelet transform
	 * @return array of time index of analyzed frames
	 * */
	public ArrayList<Integer> getAllAnalyzedTimesResults() {
		return new ArrayList<Integer>(analyzedTimes);
	}

	/**
	 * Store a set of wavelet coefficients for a determined time frame
	 * @param t the time index of the analyzed 2D image in the sequence
	 * @param result a set of wavelet coefficients
	 * */
	public void setResult(int t, WaveletAnalysisResults result) {
		int idx = -1;
		int cnt = 0;
		for (int t2 : analyzedTimes) {
			if (t2 == t) {
				idx = cnt;
				break;
			}
			cnt++;
		}
		if (idx == -1) {
			analyzedTimes.add(t);
			results.add(result);
		} else {
			results.set(idx, result);
		}
	}

	/**
	 * Get the computed wavelet coefficients for a given time index in the
	 * sequence of images
	 * @param t time index
	 * @return a set of wavelet coefficient for the frame at time t
	 * */
	public WaveletAnalysisResults getResult(int t) {
		int cnt = 0;
		for (int t2 : analyzedTimes) {
			if (t2 == t)
				return results.get(cnt);
			cnt++;
		}
		return null;
	}
	/**
	 * Reset the coefficient storage structure with a given set of wavelet
	 * coefficients
	 * @param results the set of wavelet coefficients to reset the storage
	 * structure with. Frames are assumed to start at 0 and end at
	 * results.size()-1
	 * */
	public void setAllResults(ArrayList<WaveletAnalysisResults> results) {
		this.results.clear();
		this.analyzedTimes.clear();
		this.results.addAll(results);
		for (int t = 0; t < results.size(); t++)
			this.analyzedTimes.add(t);
	}
}
