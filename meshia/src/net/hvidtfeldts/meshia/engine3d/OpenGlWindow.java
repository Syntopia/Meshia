package net.hvidtfeldts.meshia.engine3d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.Animator;

public final class OpenGlWindow extends GLCanvas implements MouseListener, MouseMotionListener, KeyListener {
    public static final boolean MEASURE_FPS = true;
    
    private static final long serialVersionUID = 1L;
    private Animator animator;
    private final Engine oglc;
    
    private int x;
    private int y;
    
    private OpenGlWindow(final GLCapabilities caps) {
        super(caps);
        
        oglc = new Engine();
        addGLEventListener(oglc);
        this.setBackground(Color.BLACK);
        
        if (MEASURE_FPS) {
            animator = new Animator();
            animator.setRunAsFastAsPossible(true);
            animator.add(this);
            animator.start();
        }
        
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }
    
    public static OpenGlWindow createOpenGlWindow() {
        GLProfile glp = GLProfile.get(GLProfile.GL2ES2);
        final GLCapabilities caps = new GLCapabilities(glp);
        caps.setSampleBuffers(true);
        caps.setNumSamples(2);
        OpenGlWindow oglw = new OpenGlWindow(caps);
        oglw.setMinimumSize(new Dimension(50, 50));
        
        return oglw;
    }
    
    public void dispose() {
        if (animator != null) {
            animator.stop();
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        oglc.rotate(x - e.getX(), y - e.getY());
        x = e.getX();
        y = e.getY();
        repaint();
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'a') {
            oglc.moveCamera(1, 0, 0);
        }
        if (e.getKeyChar() == 'd') {
            oglc.moveCamera(-1, 0, 0);
        }
        if (e.getKeyChar() == 'w') {
            oglc.moveCamera(0, 0, 1);
        }
        if (e.getKeyChar() == 's') {
            oglc.moveCamera(0, 0, -1);
        }
        repaint();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
