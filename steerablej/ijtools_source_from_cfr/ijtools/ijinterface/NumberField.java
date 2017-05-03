/*
 * Decompiled with CFR 0_118.
 */
package ijtools.ijinterface;

import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.EventListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class NumberField
extends JComponent
implements ActionListener,
DocumentListener {
    private Button plus;
    private Button minus;
    private JTextField text;
    private JLabel label;
    private double value = 1.0;
    private double increment = 0.25;
    static /* synthetic */ Class class$java$awt$event$ActionListener;

    public NumberField(double d, String string) {
        int n = 6;
        this.value = d;
        this.text = new JTextField(String.valueOf(d), n);
        this.minus = new Button("-");
        this.minus.setPreferredSize(new Dimension(21, 21));
        this.plus = new Button("+");
        this.plus.setPreferredSize(new Dimension(21, 21));
        this.label = new JLabel(string);
        this.label.setBounds(185, 0, 55, 20);
        this.label.setPreferredSize(new Dimension(55, 20));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
        this.add((Component)this.text, gridBagConstraints);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        this.add((Component)this.minus, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        this.add((Component)this.plus, gridBagConstraints);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 5, 0, 0);
        this.add((Component)this.label, gridBagConstraints);
        this.plus.addActionListener(this);
        this.minus.addActionListener(this);
        this.text.getDocument().addDocumentListener(this);
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double d) {
        this.value = d;
        this.text.setText(String.valueOf(d));
    }

    public void setLabel(String string) {
        this.label.setText(string);
    }

    public synchronized void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.plus) {
            this.value += this.value * this.increment;
            this.text.setText(String.valueOf(this.value));
        } else if (actionEvent.getSource() == this.minus) {
            this.value -= this.value * this.increment;
            this.text.setText(String.valueOf(this.value));
        }
    }

    public void addActionListener(ActionListener actionListener) {
        Class class_ = class$java$awt$event$ActionListener == null ? (NumberField.class$java$awt$event$ActionListener = NumberField.class$("java.awt.event.ActionListener")) : class$java$awt$event$ActionListener;
        this.listenerList.add(class_, actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        Class class_ = class$java$awt$event$ActionListener == null ? (NumberField.class$java$awt$event$ActionListener = NumberField.class$("java.awt.event.ActionListener")) : class$java$awt$event$ActionListener;
        this.listenerList.remove(class_, actionListener);
    }

    protected void fireActionEvent() {
        Object[] arrobject = this.listenerList.getListenerList();
        ActionEvent actionEvent = null;
        for (int i = arrobject.length - 2; i >= 0; i -= 2) {
            if (arrobject[i] != (class$java$awt$event$ActionListener == null ? NumberField.class$("java.awt.event.ActionListener") : class$java$awt$event$ActionListener)) continue;
            if (actionEvent == null) {
                actionEvent = new ActionEvent(this, 1001, "");
            }
            ((ActionListener)arrobject[i + 1]).actionPerformed(actionEvent);
        }
    }

    public void insertUpdate(DocumentEvent documentEvent) {
        Document document = documentEvent.getDocument();
        int n = document.getLength();
        if (document == this.text.getDocument() && n != 0) {
            try {
                this.value = Double.parseDouble(document.getText(0, document.getLength()));
                this.fireActionEvent();
            }
            catch (NumberFormatException var4_4) {
            }
            catch (BadLocationException var4_5) {
                System.out.println("BadLocationException: " + var4_5);
            }
        }
    }

    public void removeUpdate(DocumentEvent documentEvent) {
        Document document = documentEvent.getDocument();
        int n = document.getLength();
        if (document == this.text.getDocument() && n != 0) {
            try {
                this.value = Double.parseDouble(document.getText(0, document.getLength()));
                this.fireActionEvent();
            }
            catch (NumberFormatException var4_4) {
            }
            catch (BadLocationException var4_5) {
                System.out.println("BadLocationException: " + var4_5);
            }
        }
    }

    public void changedUpdate(DocumentEvent documentEvent) {
    }

    static /* synthetic */ Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException var1_1) {
            throw new NoClassDefFoundError(var1_1.getMessage());
        }
    }
}

