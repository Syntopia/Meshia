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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import net.hvidtfeldts.meshia.engine3d.OpenGlWindow;
import net.hvidtfeldts.utils.Logger;

public class MainWindow extends JDialog {
    private static final long serialVersionUID = 1L;
    private final OpenGlWindow openGlWindow;
    
    static {
        // Initialize OpenGL
        GLProfile.initSingleton();
    }
    
    public MainWindow() {
        setLayout(new BorderLayout());
        openGlWindow = OpenGlWindow.createOpenGlWindow();
        add(openGlWindow, BorderLayout.CENTER);
        TextAreaLogger tal = new TextAreaLogger();
        Logger.setLogger(tal);
        add(tal.createComponent(), BorderLayout.SOUTH);
        setupMenus();
        openGlWindow.setPreferredSize(new Dimension(400, 400));
        setSize(new Dimension(400, 500));
        this.invalidate();
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
            String message = obj.toString() + "\n";
            textArea.append(message);
            System.out.println(message);
        }
        
        public Component createComponent() {
            JScrollPane sp = new JScrollPane(textArea);
            sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            sp.setPreferredSize(new Dimension(50, 200));
            return sp;
        }
        
        @Override
        protected void internalWarn(Object obj) {
            String message = "Warning: " + obj.toString() + "\n";
            textArea.append(message);
            System.err.println(message);
        }
    }
}
