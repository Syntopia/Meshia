package net.hvidtfeldts.meshia;

import javax.swing.JFrame;

import net.hvidtfeldts.meshia.gui.MainWindow;

/**
 * Entry point for 'Meshia'
 */
final class Main {
    private Main() {
    }
    
    public static void main(final String[] args) {
        MainWindow mw = new MainWindow();
        mw.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mw.setVisible(true);
    }
}
