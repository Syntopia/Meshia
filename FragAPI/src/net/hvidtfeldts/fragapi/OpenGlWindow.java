package net.hvidtfeldts.fragapi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.util.Animator;

public final class OpenGlWindow extends GLJPanel implements MouseListener, MouseMotionListener, KeyListener {
    
    private static final long serialVersionUID = 1L;
    
    private Animator animator;
    private final Engine engine;
    
    private int x;
    private int y;
    
    private OpenGlWindow(final GLCapabilities caps, Engine engine) {
        super(caps);
        this.engine = engine;
        addGLEventListener(engine);
        this.setBackground(Color.BLACK);
        
        if (Defaults.getMeasureFPS()) {
            animator = new Animator();
            animator.setRunAsFastAsPossible(true);
            animator.add(this);
            animator.start();
        }
        
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        
    }
    
    public static OpenGlWindow create(FrameBufferBase fb) {
        fb.setAsOutputBuffer();
        Engine engine = new Engine();
        engine.setOutputBuffer(fb);
        GLProfile glp = GLProfile.get(GLProfile.GL2ES2);
        final GLCapabilities caps = new GLCapabilities(glp);
        caps.setSampleBuffers(true);
        caps.setNumSamples(2);
        OpenGlWindow oglw = new OpenGlWindow(caps, engine);
        oglw.setMinimumSize(new Dimension(50, 50));
        
        return oglw;
    }
    
    @Override
    public void dispose() {
        if (animator != null) {
            animator.stop();
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocus();
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
        if (SwingUtilities.isLeftMouseButton(e) && SwingUtilities.isRightMouseButton(e)) {
            moveCamera(0, 0, (e.getY() - y) / 2.0f);
            Camera.getInstance().zoom((e.getX() - x) / 10.0f);
        }
        else if (SwingUtilities.isLeftMouseButton(e)) {
            rotate(x - e.getX(), y - e.getY());
        }
        
        else {
            moveCamera(-(x - e.getX()) / 10.f, (y - e.getY()) / 10.f, 0.0f);
        }
        x = e.getX();
        y = e.getY();
        repaint();
    }
    
    public void rotate(int i, int j) {
        Camera.getInstance().rotateAboutRight(j / 100.0f);
        Camera.getInstance().rotateAboutUp(i / 100.0f);
    }
    
    public void moveCamera(float x, float y, float z) {
        Camera.getInstance().move(x / 3.0f, y / 3.0f, z / 3.0f);
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'a') {
            moveCamera(1, 0, 0);
        }
        else if (e.getKeyChar() == 'd') {
            moveCamera(-1, 0, 0);
        }
        else if (e.getKeyChar() == 'w') {
            moveCamera(0, 0, 1);
        }
        else if (e.getKeyChar() == 's') {
            moveCamera(0, 0, -1);
        }
        repaint();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
