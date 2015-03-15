package net.hvidtfeldts.fragapi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.opengl.GLProfile;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.hvidtfeldts.utils.Logger;

public class FrameBufferWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private final OpenGlWindow openGlWindow;
    
    static {
        // Initialize OpenGL
        GLProfile.initSingleton();
    }
    
    public FrameBufferWindow(FrameBuffer outputBuffer) {
        setLayout(new BorderLayout());
        setTitle("FragAPI");
        
        List<Image> icons = new ArrayList<>();
        try {
            icons.add(ImageIO.read(getClass().getResourceAsStream("/icons/icon16.png")));
            icons.add(ImageIO.read(getClass().getResourceAsStream("/icons/icon24.png")));
            icons.add(ImageIO.read(getClass().getResourceAsStream("/icons/icon32.png")));
            icons.add(ImageIO.read(getClass().getResourceAsStream("/icons/icon48.png")));
            icons.add(ImageIO.read(getClass().getResourceAsStream("/icons/icon256.png")));
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        setIconImages(icons);
        
        openGlWindow = OpenGlWindow.create(outputBuffer);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(openGlWindow);
        splitPane.add(createButtonsAndLogPanel());
        splitPane.setDividerLocation(300);
        
        add(splitPane, BorderLayout.CENTER);
        
        setupMenus();
        openGlWindow.setPreferredSize(new Dimension(400, 400));
        setSize(new Dimension(400, 500));
        this.invalidate();
    }
    
    public static void show(final FrameBuffer outputBuffer) {
        setNativeLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                FrameBufferWindow mw = new FrameBufferWindow(outputBuffer);
                mw.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                mw.setVisible(true);
                mw.setSize(430, 700);
            }
            
        });
        
    }
    
    private static void setNativeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    private JPanel createButtonsAndLogPanel() {
        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());
        TextAreaLogger tal = new TextAreaLogger();
        Logger.setLogger(tal);
        jp.add(tal.createComponent(), BorderLayout.CENTER);
        return jp;
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
                FrameBufferWindow.this.dispose();
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
