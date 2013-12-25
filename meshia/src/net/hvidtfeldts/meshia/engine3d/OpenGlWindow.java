package net.hvidtfeldts.meshia.engine3d;

import java.awt.Color;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.Animator;

public final class OpenGlWindow extends GLCanvas {
    private static final long serialVersionUID = 1L;
    private final Animator animator;
    
    private OpenGlWindow(final GLCapabilities caps) {
        super(caps);
        
        OpenGlController oglc = new OpenGlController();
        addGLEventListener(oglc);
        this.setBackground(Color.BLACK);
        
        animator = new Animator();
        animator.add(this);
        animator.start();
    }
    
    public static OpenGlWindow createOpenGlWindow() {
        GLProfile glp = GLProfile.get(GLProfile.GL2);
        final GLCapabilities caps = new GLCapabilities(glp);
        caps.setSampleBuffers(true);
        caps.setNumSamples(2);
        OpenGlWindow oglw = new OpenGlWindow(caps);
        return oglw;
    }
    
    public void dispose() {
        animator.stop();
    }
    
}
