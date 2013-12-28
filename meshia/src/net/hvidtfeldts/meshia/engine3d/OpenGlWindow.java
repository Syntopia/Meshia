package net.hvidtfeldts.meshia.engine3d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import net.hvidtfeldts.utils.Logger;

import com.jogamp.opengl.util.Animator;

public final class OpenGlWindow extends GLCanvas implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
    private final Animator animator;
    OpenGlController oglc;
    
    private OpenGlWindow(final GLCapabilities caps) {
        super(caps);
        
        oglc = new OpenGlController();
        addGLEventListener(oglc);
        this.setBackground(Color.BLACK);
        animator = new Animator();
        animator.setRunAsFastAsPossible(true);
        animator.add(this);
        animator.start();
        
        addMouseListener(this);
        addMouseMotionListener(this);
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
        animator.stop();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    boolean inDrag;
    int x;
    int y;
    
    @Override
    public void mousePressed(MouseEvent e) {
        inDrag = true;
        x = e.getX();
        y = e.getY();
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        inDrag = false;
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        
        Logger.log((x - e.getX()) + " " + (y - e.getY()));
        oglc.rotate(x - e.getX(), y - e.getY());
        x = e.getX();
        y = e.getY();
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
