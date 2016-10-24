/*
 * Decompiled with CFR 0_118.
 */
package additionaluserinterface;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;

public class WalkBar
extends JToolBar
implements ActionListener {
    private JProgressBar progress = new JProgressBar();
    private JButton bnHelp = new JButton("Help");
    private JButton bnAbout = new JButton("About");
    private JButton bnClose = new JButton("Close");
    private String[] about = new String[]{"About", "Version", "Description", "Author", "Biomedical Image Group", "2008", "http://bigwww.epfl.ch"};
    private String help;
    private double chrono;

    public WalkBar(String string, boolean bl, boolean bl2, boolean bl3) {
        super("Walk Bar");
        this.build(string, bl, bl2, bl3, 100);
    }

    public WalkBar(String string, boolean bl, boolean bl2, boolean bl3, int n) {
        super("Walk Bar");
        this.build(string, bl, bl2, bl3, n);
    }

    private void build(String string, boolean bl, boolean bl2, boolean bl3, int n) {
        if (bl) {
            this.add(this.bnAbout);
        }
        if (bl2) {
            this.add(this.bnHelp);
        }
        this.addSeparator();
        this.add(this.progress);
        this.addSeparator();
        if (bl3) {
            this.add(this.bnClose);
        }
        this.progress.setStringPainted(true);
        this.progress.setString(string);
        this.progress.setFont(new Font("Arial", 0, 10));
        this.progress.setMinimum(0);
        this.progress.setMaximum(100);
        this.progress.setPreferredSize(new Dimension(n, 20));
        this.bnAbout.addActionListener(this);
        this.bnHelp.addActionListener(this);
        this.setFloatable(false);
        this.setRollover(true);
        this.setBorderPainted(false);
        this.chrono = System.currentTimeMillis();
    }

    public synchronized void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (actionEvent.getSource() == this.bnHelp) {
            this.showHelp();
        } else if (actionEvent.getSource() == this.bnAbout) {
            this.showAbout();
        } else if (actionEvent.getSource() == this.bnClose) {
            // empty if block
        }
    }

    public JButton getButtonClose() {
        return this.bnClose;
    }

    public void setValue(int n) {
        this.progress.setValue(n);
    }

    public void setMessage(String string) {
        this.progress.setString(string);
    }

    public void progress(String string, int n) {
        this.progress.setValue(n);
        double d = (double)System.currentTimeMillis() - this.chrono;
        String string2 = " [" + (d > 3000.0 ? new StringBuilder().append((double)Math.round(d / 10.0) / 100.0).append("s.").toString() : new StringBuilder().append(d).append("ms").toString()) + "]";
        this.progress.setString(string + string2);
    }

    public void progress(String string, double d) {
        this.progress(string, (int)Math.round(d));
    }

    public void reset() {
        this.chrono = System.currentTimeMillis();
        this.progress.setValue(0);
        this.progress.setString("Starting ...");
    }

    public void finish() {
        this.progress("Terminated", 100);
    }

    public void finish(String string) {
        this.progress(string, 100);
    }

    public void fillAbout(String string, String string2, String string3, String string4, String string5, String string6, String string7) {
        this.about[0] = string;
        this.about[1] = string2;
        this.about[2] = string3;
        this.about[3] = string4;
        this.about[4] = string5;
        this.about[5] = string6;
        this.about[6] = string7;
    }

    public void fillHelp(String string) {
        this.help = string;
    }

    public void showAbout() {
        final JFrame jFrame = new JFrame("About " + this.about[0]);
        JEditorPane jEditorPane = new JEditorPane();
        jEditorPane.setEditable(false);
        jEditorPane.setContentType("text/html; charset=ISO-8859-1");
        jEditorPane.setText("<html><head><title>" + this.about[0] + "</title>" + this.getStyle() + "</head><body>" + (this.about[0] == "" ? "" : new StringBuilder().append("<p class=\"name\">").append(this.about[0]).append("</p>").toString()) + (this.about[1] == "" ? "" : new StringBuilder().append("<p class=\"vers\">").append(this.about[1]).append("</p>").toString()) + (this.about[2] == "" ? "" : new StringBuilder().append("<p class=\"desc\">").append(this.about[2]).append("</p><hr>").toString()) + (this.about[3] == "" ? "" : new StringBuilder().append("<p class=\"auth\">").append(this.about[3]).append("</p>").toString()) + (this.about[4] == "" ? "" : new StringBuilder().append("<p class=\"orga\">").append(this.about[4]).append("</p>").toString()) + (this.about[5] == "" ? "" : new StringBuilder().append("<p class=\"date\">").append(this.about[5]).append("</p>").toString()) + (this.about[6] == "" ? "" : new StringBuilder().append("<p class=\"more\">").append(this.about[6]).append("</p>").toString()) + "</html>");
        JButton jButton = new JButton("Close");
        jButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                jFrame.dispose();
            }
        });
        jEditorPane.setCaret(new DefaultCaret());
        JScrollPane jScrollPane = new JScrollPane(jEditorPane);
        jScrollPane.setPreferredSize(new Dimension(400, 400));
        jFrame.getContentPane().add((Component)jScrollPane, "North");
        jFrame.getContentPane().add((Component)jButton, "Center");
        jFrame.pack();
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        this.center(jFrame);
    }

    public void showHelp() {
        JFrame jFrame = new JFrame("Help " + this.about[0]);
        JEditorPane jEditorPane = new JEditorPane();
        jEditorPane.setEditable(false);
        jEditorPane.setContentType("text/html; charset=ISO-8859-1");
        jEditorPane.setText("<html><head><title>" + this.about[0] + "</title>" + this.getStyle() + "</head><body>" + (this.about[0] == "" ? "" : new StringBuilder().append("<p class=\"name\">").append(this.about[0]).append("</p>").toString()) + (this.about[1] == "" ? "" : new StringBuilder().append("<p class=\"vers\">").append(this.about[1]).append("</p>").toString()) + (this.about[2] == "" ? "" : new StringBuilder().append("<p class=\"desc\">").append(this.about[2]).append("</p><hr>").toString()) + (this.about[3] == "" ? "" : new StringBuilder().append("<p class=\"auth\">").append(this.about[3]).append("</p>").toString()) + (this.about[4] == "" ? "" : new StringBuilder().append("<p class=\"orga\">").append(this.about[4]).append("</p>").toString()) + (this.about[5] == "" ? "" : new StringBuilder().append("<p class=\"date\">").append(this.about[5]).append("</p>").toString()) + (this.about[6] == "" ? "" : new StringBuilder().append("<p class=\"more\">").append(this.about[6]).append("</p>").toString()) + "<hr><p class=\"help\">" + this.help + "</p>" + "</html>");
        jEditorPane.setCaret(new DefaultCaret());
        JScrollPane jScrollPane = new JScrollPane(jEditorPane);
        jScrollPane.setVerticalScrollBarPolicy(22);
        jScrollPane.setPreferredSize(new Dimension(400, 600));
        jFrame.setPreferredSize(new Dimension(400, 600));
        jFrame.getContentPane().add((Component)jScrollPane, "Center");
        jFrame.setVisible(true);
        jFrame.pack();
        this.center(jFrame);
    }

    private void center(Window window) {
        Object object;
        Dimension dimension = new Dimension(0, 0);
        boolean bl = System.getProperty("os.name").startsWith("Windows");
        if (bl) {
            dimension = Toolkit.getDefaultToolkit().getScreenSize();
        }
        if (GraphicsEnvironment.isHeadless()) {
            dimension = new Dimension(0, 0);
        } else {
            object = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] arrgraphicsDevice = object.getScreenDevices();
            GraphicsConfiguration[] arrgraphicsConfiguration = arrgraphicsDevice[0].getConfigurations();
            Rectangle rectangle = arrgraphicsConfiguration[0].getBounds();
            dimension = rectangle.x == 0 && rectangle.y == 0 ? new Dimension(rectangle.width, rectangle.height) : Toolkit.getDefaultToolkit().getScreenSize();
        }
        object = window.getSize();
        if (object.width == 0) {
            return;
        }
        int n = dimension.width / 2 - object.width / 2;
        int n2 = (dimension.height - object.height) / 4;
        if (n2 < 0) {
            n2 = 0;
        }
        window.setLocation(n, n2);
    }

    private String getStyle() {
        return "<style type=text/css>body {backgroud-color:#222277}hr {width:80% color:#333366; padding-top:7px }p, li {margin-left:10px;margin-right:10px; color:#000000; font-size:1em; font-family:Verdana,Helvetica,Arial,Geneva,Swiss,SunSans-Regular,sans-serif}p.name {color:#ffffff; font-size:1.2em; font-weight: bold; background-color: #333366; text-align:center;}p.vers {color:#333333; text-align:center;}p.desc {color:#333333; font-weight: bold; text-align:center;}p.auth {color:#333333; font-style: italic; text-align:center;}p.orga {color:#333333; text-align:center;}p.date {color:#333333; text-align:center;}p.more {color:#333333; text-align:center;}p.help {color:#000000; text-align:left;}</style>";
    }

}

