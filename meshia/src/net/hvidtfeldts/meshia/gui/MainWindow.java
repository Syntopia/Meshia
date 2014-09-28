package net.hvidtfeldts.meshia.gui;

import groovy.lang.GroovyClassLoader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.opengl.GLProfile;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ProgressMonitor;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import net.hvidtfeldts.meshia.engine3d.Engine;
import net.hvidtfeldts.meshia.engine3d.Object3D;
import net.hvidtfeldts.meshia.engine3d.OpenGlWindow;
import net.hvidtfeldts.meshia.engine3d.SimpleMarchingCubes;
import net.hvidtfeldts.meshia.gui.TextDialog.CloseAction;
import net.hvidtfeldts.meshia.math.Vector3;
import net.hvidtfeldts.utils.Logger;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private final OpenGlWindow openGlWindow;
    
    private final Project project = new Project();
    private final Engine engine;
    
    static {
        // Initialize OpenGL
        GLProfile.initSingleton();
    }
    
    public MainWindow() {
        setLayout(new BorderLayout());
        setTitle("Meshia - Mesh Generator");
        
        List<Image> icons = new ArrayList<>();
        try {
            icons.add(ImageIO.read(getClass().getResourceAsStream("/res/icons/icon16.png")));
            icons.add(ImageIO.read(getClass().getResourceAsStream("/res/icons/icon24.png")));
            icons.add(ImageIO.read(getClass().getResourceAsStream("/res/icons/icon32.png")));
            icons.add(ImageIO.read(getClass().getResourceAsStream("/res/icons/icon48.png")));
            icons.add(ImageIO.read(getClass().getResourceAsStream("/res/icons/icon256.png")));
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        setIconImages(icons);
        
        engine = new Engine(project);
        
        openGlWindow = OpenGlWindow.createOpenGlWindow(engine);
        engine.setPanel(openGlWindow);
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(openGlWindow);
        splitPane.add(createButtonsAndLogPanel());
        splitPane.setDividerLocation(300);
        
        ProjectView pw = new ProjectView();
        pw.setModel(project);
        mainSplitPane.setDividerLocation(150);
        mainSplitPane.add(pw);
        mainSplitPane.add(splitPane);
        
        add(mainSplitPane, BorderLayout.CENTER);
        
        setupMenus();
        openGlWindow.setPreferredSize(new Dimension(400, 400));
        setSize(new Dimension(400, 500));
        this.invalidate();
    }
    
    private JPanel createButtonsAndLogPanel() {
        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());
        JPanel buttonPanel = createButtonPanel();
        jp.add(buttonPanel, BorderLayout.NORTH);
        TextAreaLogger tal = new TextAreaLogger();
        Logger.setLogger(tal);
        jp.add(tal.createComponent(), BorderLayout.CENTER);
        return jp;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        final JButton inputButton = new JButton("Input");
        buttonPanel.add(inputButton);
        
        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu p = new JPopupMenu();
                JMenuItem ji = new JMenuItem("Marching Cubes from Images");
                p.add(ji);
                ji.setEnabled(false);
                JMenuItem ji2 = new JMenuItem("Marching Cubes from GPU GLSL");
                p.add(ji2);
                ji2.setEnabled(false);
                JMenuItem ji3 = new JMenuItem("Marching Cubes from Java (Groovy)");
                p.add(ji3);
                ji3.setEnabled(true);
                ji3.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        doMarchingCubesFromJava();
                    }
                    
                });
                JMenuItem ji4 = new JMenuItem("Shader Object (GLSL DE)");
                p.add(ji4);
                ji4.setEnabled(false);
                JMenuItem ji5 = new JMenuItem("Shader Object (GLSL Brute Force)");
                p.add(ji5);
                ji5.setEnabled(false);
                p.show(inputButton, 0, 0);
                p.show(inputButton, 0, -p.getHeight());
            }
        });
        
        JButton modifyButton = new JButton("Modify");
        buttonPanel.add(modifyButton);
        
        /*
         * Modify -> Apply Hemesh Modification
         * -> Delete
         * 
         * Output -> to OBJ
         * -> to STL
         * -> Sunflow Render
         */
        
        JButton outputButton = new JButton("Output");
        buttonPanel.add(outputButton);
        return buttonPanel;
    }
    
    private void doMarchingCubesFromJava() {
        
        TextDialog td = new TextDialog();
        td.setModal(true);
        td.setVisible(true);
        
        if (td.getCloseAction() == CloseAction.OK) {
            GroovyClassLoader gcl = new GroovyClassLoader();
            Class clazz = gcl.parseClass(td.getCode());
            SimpleMarchingCubes smc = null;
            try {
                smc = (SimpleMarchingCubes) clazz.newInstance();
                gcl.close();
            }
            catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            
            if (smc == null) {
                return;
            }
            
            int res = td.getGridSize();
            final SimpleMarchingCubes simpleMarchingCubes = smc;
            simpleMarchingCubes.initMarchingCubes(0, res, res, res, MainWindow.this, new Vector3(-1, -1, -1),
                    new Vector3(1, 1, 1));
            
            final ProgressMonitor pm = new ProgressMonitor(MainWindow.this, "Marching...", "Polygonizing", 0, res);
            pm.setMillisToDecideToPopup(1);
            Thread t = new Thread() {
                @Override
                public void run() {
                    final Object3D o = simpleMarchingCubes.getObject3D(engine.getShaderState(), pm);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            project.addObject(o);
                        }
                    });
                };
            };
            
            t.start();
        }
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
