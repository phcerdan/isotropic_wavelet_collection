package plugins.nchenouard.isotropicwavelets;

import icy.gui.component.pool.SwimmingObjectChooser;
import icy.swimmingPool.SwimmingObject;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 *
 * GUI for wavelet coefficients manual selection
 *
 * @author Nicolas Chenouard (nicolas.chenouard.dev@gmail.com)
 * @version 1.0
 * @date 2014-05-22
 * @license gpl v3.0
 */

public class CoefficientSelectionPanel extends JPanel {
    private static final long serialVersionUID = -4324385449999084787L;
    CoefficientsTableModel coeffTableModel;
    JTable coefficientTable;
    SwimmingObjectChooser resultsBox;

    public CoefficientSelectionPanel() {
	this.setLayout(new BorderLayout());
	resultsBox = new SwimmingObjectChooser(SequenceAnalysisResults.class);
	this.add(resultsBox, BorderLayout.NORTH);
	resultsBox.addItemListener(new ItemListener() {
	    @Override
	    public void itemStateChanged(ItemEvent arg0) {
		updateResultsTable();
	    }
	});

	coeffTableModel = new CoefficientsTableModel();
	coefficientTable = new JTable(coeffTableModel);
	JScrollPane scrollTablePane = new JScrollPane(coefficientTable);
	this.add(scrollTablePane, BorderLayout.CENTER);
    }

    void updateResultsTable() {
	Object selectedItem = resultsBox.getSelectedItem();
	if (selectedItem != null) {
	    SequenceAnalysisResults results =
		(SequenceAnalysisResults) ((SwimmingObject) selectedItem)
		.getObject();
	    coeffTableModel.setResults(results);
	} else
	    coeffTableModel.setResults(null);
    }

    /**Table model for wavelet coefficients*/
    public class CoefficientsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -4810024326298005026L;
	SequenceAnalysisResults results = null;
	String[] columnNames = new String[] {"Sequence name", "Frames",
	    "Scales", "Wavelet profile", "Filter high frequencies",
	    "Isotropic padding", "Padding X", "Padding Y"};
	String[] valueList = new String[] {"", "", "", "", "", "", "", ""};

	@Override
	public String getColumnName(int column) {
	    return columnNames[column];
	};

	public void setResults(SequenceAnalysisResults r) {
	    results = r;
	    if (results == null) {
		for (int i = 0; i < valueList.length; i++)
		    valueList[i] = "";
	    } else {
		valueList[0] = results.getSequenceName();
		valueList[1] = Integer.toString(
			results.getAllAnalyzedTimesResults().size());
		WaveletAnalysisResults wr = results.getAllResults().get(0);
		WaveletFilterSet filters = wr.getWaveletFilters();
		valueList[2] = Integer.toString(filters.getNumScales());
		valueList[3] = filters.getWaveletType().toString();
		valueList[4] = Boolean.toString(filters.isPrefilter());
		valueList[5] = Boolean.toString(filters.isIsotropicPadding());
		valueList[6] = Integer.toString(wr.getPadX());
		valueList[7] = Integer.toString(wr.getPadY());
	    }
	    this.fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
	    return columnNames.length;
	}

	@Override
	public int getRowCount() {
	    return 1;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
	    return valueList[arg1];
	}
    }
}
