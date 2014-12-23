/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vzdelavaniefetcher.tools;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Unlink
 */
public class GUISwingTools {
    
    /**
     * Zobrazí chybovú hlášku
     * @param parentComponent
     * @param message 
     */
    public static void displayErrorMessage(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(parentComponent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Zobrazí informacnu hlášku
     * @param parentComponent
     * @param message 
     */
    public static void displayInfoMessage(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(parentComponent, message, "Informácia", JOptionPane.INFORMATION_MESSAGE);
    }
}
