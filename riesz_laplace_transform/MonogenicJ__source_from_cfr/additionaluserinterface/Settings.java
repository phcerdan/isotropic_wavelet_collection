/*
 * Decompiled with CFR 0_118.
 */
package additionaluserinterface;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.Timer;

public class Settings {
    private String filename;
    private String project;
    private Vector items;
    private Properties props;

    public Settings(String string, String string2) {
        this.filename = string2;
        this.project = string;
        this.items = new Vector();
        this.props = new Properties();
    }

    public void record(String string, JTextField jTextField, String string2) {
        Item item = new Item(string, jTextField, string2);
        this.items.add(item);
    }

    public void record(String string, JComboBox jComboBox, String string2) {
        Item item = new Item(string, jComboBox, string2);
        this.items.add(item);
    }

    public void record(String string, JSpinner jSpinner, String string2) {
        Item item = new Item(string, jSpinner, string2);
        this.items.add(item);
    }

    public void record(String string, JToggleButton jToggleButton, boolean bl) {
        Item item = new Item(string, jToggleButton, bl ? "on" : "off");
        this.items.add(item);
    }

    public void record(String string, JCheckBox jCheckBox, boolean bl) {
        Item item = new Item(string, jCheckBox, bl ? "on" : "off");
        this.items.add(item);
    }

    public void record(String string, JSlider jSlider, String string2) {
        Item item = new Item(string, jSlider, string2);
        this.items.add(item);
    }

    public String loadValue(String string, String string2) {
        String string3 = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(this.filename);
            this.props.load(fileInputStream);
            string3 = this.props.getProperty(string, "" + string2);
        }
        catch (Exception var4_5) {
            string3 = string2;
        }
        return string3;
    }

    public double loadValue(String string, double d) {
        double d2 = 0.0;
        try {
            FileInputStream fileInputStream = new FileInputStream(this.filename);
            this.props.load(fileInputStream);
            String string2 = this.props.getProperty(string, "" + d);
            d2 = new Double(string2);
        }
        catch (Exception var6_5) {
            d2 = d;
        }
        return d2;
    }

    public int loadValue(String string, int n) {
        int n2 = 0;
        try {
            FileInputStream fileInputStream = new FileInputStream(this.filename);
            this.props.load(fileInputStream);
            String string2 = this.props.getProperty(string, "" + n);
            n2 = new Integer(string2);
        }
        catch (Exception var4_5) {
            n2 = n;
        }
        return n2;
    }

    public boolean loadValue(String string, boolean bl) {
        boolean bl2 = false;
        try {
            FileInputStream fileInputStream = new FileInputStream(this.filename);
            this.props.load(fileInputStream);
            String string2 = this.props.getProperty(string, "" + bl);
            bl2 = new Boolean(string2);
        }
        catch (Exception var4_5) {
            bl2 = bl;
        }
        return bl2;
    }

    public void storeValue(String string, String string2) {
        this.props.setProperty(string, string2);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.filename);
            this.props.store(fileOutputStream, this.project);
        }
        catch (Exception var3_4) {
            new Msg(this.project, "Impossible to store settings in (" + this.filename + ")");
        }
    }

    public void storeValue(String string, double d) {
        this.props.setProperty(string, "" + d);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.filename);
            this.props.store(fileOutputStream, this.project);
        }
        catch (Exception var4_4) {
            new Msg(this.project, "Impossible to store settings in (" + this.filename + ")");
        }
    }

    public void storeValue(String string, int n) {
        this.props.setProperty(string, "" + n);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.filename);
            this.props.store(fileOutputStream, this.project);
        }
        catch (Exception var3_4) {
            new Msg(this.project, "Impossible to store settings in (" + this.filename + ")");
        }
    }

    public void storeValue(String string, boolean bl) {
        this.props.setProperty(string, "" + bl);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.filename);
            this.props.store(fileOutputStream, this.project);
        }
        catch (Exception var3_4) {
            new Msg(this.project, "Impossible to store settings in (" + this.filename + ")");
        }
    }

    public void loadRecordedItems() {
        try {
            FileInputStream fileInputStream = new FileInputStream(this.filename);
            this.props.load(fileInputStream);
        }
        catch (Exception var1_2) {
            new Msg(this.project, "Loading default value. No settings file (" + this.filename + ")");
        }
        for (int i = 0; i < this.items.size(); ++i) {
            Item item = (Item)this.items.get(i);
            String string = this.props.getProperty(item.key, item.defaultValue);
            if (item.component instanceof JTextField) {
                ((JTextField)item.component).setText(string);
                continue;
            }
            if (item.component instanceof JComboBox) {
                ((JComboBox)item.component).setSelectedItem(string);
                continue;
            }
            if (item.component instanceof JCheckBox) {
                ((JCheckBox)item.component).setSelected(string.equals("on"));
                continue;
            }
            if (item.component instanceof JToggleButton) {
                ((JToggleButton)item.component).setSelected(string.equals("on"));
                continue;
            }
            if (item.component instanceof JSpinner) {
                ((JSpinner)item.component).setValue((double)new Double(string));
                continue;
            }
            if (!(item.component instanceof JSlider)) continue;
            ((JSlider)item.component).setValue(new Integer(string));
        }
    }

    public void storeRecordedItems() {
        for (int i = 0; i < this.items.size(); ++i) {
            String string;
            Item item = (Item)this.items.get(i);
            if (item.component instanceof JTextField) {
                string = ((JTextField)item.component).getText();
                this.props.setProperty(item.key, string);
                continue;
            }
            if (item.component instanceof JComboBox) {
                string = (String)((JComboBox)item.component).getSelectedItem();
                this.props.setProperty(item.key, string);
                continue;
            }
            if (item.component instanceof JCheckBox) {
                string = ((JCheckBox)item.component).isSelected() ? "on" : "off";
                this.props.setProperty(item.key, string);
                continue;
            }
            if (item.component instanceof JToggleButton) {
                string = ((JToggleButton)item.component).isSelected() ? "on" : "off";
                this.props.setProperty(item.key, string);
                continue;
            }
            if (item.component instanceof JSpinner) {
                string = "" + ((JSpinner)item.component).getValue();
                this.props.setProperty(item.key, string);
                continue;
            }
            if (!(item.component instanceof JSlider)) continue;
            string = "" + ((JSlider)item.component).getValue();
            this.props.setProperty(item.key, string);
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.filename);
            this.props.store(fileOutputStream, this.project);
        }
        catch (Exception var1_3) {
            new Msg(this.project, "Impossible to store settings in (" + this.filename + ")");
        }
    }

    private class DelayListener
    implements ActionListener {
        private Msg msg;

        public DelayListener(Msg msg) {
            this.msg = msg;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            this.msg.dispose();
        }
    }

    private class Msg
    extends JFrame {
        public Msg(String string, String string2) {
            super(string);
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            Container container = this.getContentPane();
            container.setLayout(gridBagLayout);
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.gridheight = 1;
            gridBagConstraints.insets = new Insets(10, 10, 10, 10);
            gridBagConstraints.anchor = 10;
            JLabel jLabel = new JLabel(string2);
            gridBagLayout.setConstraints(jLabel, gridBagConstraints);
            container.add(jLabel);
            this.setResizable(false);
            this.pack();
            this.setVisible(true);
            Dimension dimension = this.getToolkit().getScreenSize();
            Rectangle rectangle = this.getBounds();
            this.setLocation((dimension.width - rectangle.width) / 2, (dimension.height - rectangle.height) / 2);
            Timer timer = new Timer(1000, new DelayListener(this));
            timer.start();
        }
    }

    private class Item {
        public Object component;
        public String defaultValue;
        public String key;

        public Item(String string, Object object, String string2) {
            this.component = object;
            this.defaultValue = string2;
            this.key = string;
        }
    }

}

