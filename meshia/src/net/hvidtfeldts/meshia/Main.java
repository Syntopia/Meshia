package net.hvidtfeldts.meshia;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.hvidtfeldts.meshia.gui.MainWindow;

/**
 * Entry point for 'Meshia'
 */
final class Main {
    private Main() {
    }
    
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
        MainWindow mw = new MainWindow();
        mw.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mw.setVisible(true);
    }
}
