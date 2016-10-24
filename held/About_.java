/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  ij.IJ
 *  ij.plugin.PlugIn
 */
package monogenicwavelettoolbox;

import ij.IJ;
import ij.plugin.PlugIn;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class About_
implements PlugIn {
    public void run(String arg) {
        About_.showAbout();
    }

    public static void showAbout() {
        IJ.showMessage((String)"About", (String)About_.textParser("about.txt"));
    }

    public static String textParser(String filename) {
        StringBuilder contents = new StringBuilder("");
        try {
            InputStream in = About_.class.getResourceAsStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                contents.append(line);
                if (line.trim().equals("")) {
                    contents.append(" \n");
                }
                contents.append(System.getProperty("line.separator"));
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return contents.toString();
    }
}

