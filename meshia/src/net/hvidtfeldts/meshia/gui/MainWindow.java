package net.hvidtfeldts.meshia.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.media.opengl.GLProfile;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import net.hvidtfeldts.meshia.engine3d.OpenGlWindow;
import net.hvidtfeldts.utils.Logger;

public class MainWindow extends JDialog {
    private static final long serialVersionUID = 1L;
    private final OpenGlWindow openGlWindow;
    
    static {
        GLProfile.initSingleton();
    }
    
    public MainWindow() {
        setMinimumSize(new Dimension(100, 100));
        
        setLayout(new BorderLayout());
        openGlWindow = OpenGlWindow.createOpenGlWindow();
        add(openGlWindow, BorderLayout.CENTER);
        TextAreaLogger tal = new TextAreaLogger();
        Logger.setLogger(tal);
        add(tal.getComponent(), BorderLayout.SOUTH);
        setupMenus();
    }
    
    @Override
    public void dispose() {
        openGlWindow.dispose();
        super.dispose();
    }
    
    private void setupMenus() {
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menuBar.add(menu);
        
        menuItem = new JMenuItem("Exit", KeyEvent.VK_T);
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                MainWindow.this.dispose();
            }
        });
        setJMenuBar(menuBar);
    }
    
    private static class TextAreaLogger extends Logger {
        private final JTextArea textArea = new JTextArea();
        
        @Override
        protected void internalLog(Object obj) {
            textArea.append(obj.toString() + "\n");
        }
        
        public Component getComponent() {
            return textArea;
        }
        
        @Override
        protected void internalWarn(Object obj) {
            textArea.append("Warning: " + obj.toString() + "\n");
        }
    }
}
