/*
 * Decompiled with CFR 0_118.
 */
package additionaluserinterface;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class NumericTable
extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public NumericTable(String string, String[] arrstring, Dimension dimension) {
        super(string);
        this.setMinimumSize(dimension);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        JScrollPane jScrollPane = new JScrollPane(22, 30);
        this.model = new DefaultTableModel();
        this.table = new JTable(this.model);
        for (int i = 0; i < arrstring.length; ++i) {
            this.model.addColumn(arrstring[i]);
        }
        this.table.setAutoResizeMode(0);
        jScrollPane.getViewport().add((Component)this.table, (Object)null);
        this.add(jScrollPane);
    }

    public void setData(double[][] arrd) {
        int n = arrd.length;
        int n2 = arrd[0].length;
        Object[] arrobject = new String[n2];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n2; ++j) {
                arrobject[j] = "" + arrd[i][j];
            }
            this.model.addRow(arrobject);
        }
    }

    public void setData(double[][] arrd, String[] arrstring) {
        int n = arrd.length;
        int n2 = arrd[0].length;
        Object[] arrobject = new String[n2];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n2; ++j) {
                arrobject[j] = new DecimalFormat(arrstring[j]).format(arrd[i][j]);
            }
            this.model.addRow(arrobject);
        }
    }

    public void setColumnSize(int[] arrn) {
        for (int i = 0; i < arrn.length; ++i) {
            TableColumn tableColumn = this.table.getColumnModel().getColumn(i);
            tableColumn.setPreferredWidth(arrn[i]);
        }
    }

    public void show(int n, int n2) {
        this.pack();
        this.setLocation(new Point(n, n2));
        this.setVisible(true);
    }
}

