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
import javax.media.opengl.awt.GLJPanel;
import javax.swing.SwingUtilities;

import net.hvidtfeldts.meshia.sunflow.SunflowRenderer;
import net.hvidtfeldts.meshia.sunflow.TestScene;
import net.hvidtfeldts.utils.Logger;

import org.sunflow.math.Point3;
import org.sunflow.math.Vector3;

import com.jogamp.opengl.util.Animator;

public final class OpenGlWindow extends GLJPanel implements MouseListener, MouseMotionListener, KeyListener {
    public static final boolean MEASURE_FPS = false;
    
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
    
    public static OpenGlWindow createOpenGlWindow(Engine engine) {
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
        if (SwingUtilities.isLeftMouseButton(e)) {
            engine.rotate(x - e.getX(), y - e.getY());
        }
        else {
            engine.moveCamera(-(x - e.getX()) / 10.f, (y - e.getY()) / 10.f, 0.0f);
        }
        x = e.getX();
        y = e.getY();
        repaint();
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
            engine.moveCamera(1, 0, 0);
        }
        else if (e.getKeyChar() == 'd') {
            engine.moveCamera(-1, 0, 0);
        }
        else if (e.getKeyChar() == 'w') {
            engine.moveCamera(0, 0, 1);
        }
        else if (e.getKeyChar() == 's') {
            engine.moveCamera(0, 0, -1);
        }
        else if (e.getKeyChar() == '1') {
            engine.takeSnapshot();
        }
        else if (e.getKeyChar() == '2') {
            TestScene ts = new TestScene();
            float[] pos = engine.getCamera().getPosInWorldCoords();
            float[] cpos = engine.getCamera().getPosInCameraCoords();
            float[] up = engine.getCamera().getUp();
            float[] forward = engine.getCamera().getForward();
            ts.up = new Vector3(up[0], up[1], up[2]);
            ts.eye = new Point3(pos[0], pos[1], pos[2]);
            ts.target = new Point3(pos[0] + forward[0], pos[1] + forward[1], pos[2] + forward[2]);
            Logger.log(String.format("Forward: %s %s %s", forward[0], forward[1], forward[2]));
            Logger.log(String.format("Pos: %s %s %s", pos[0], pos[1], pos[2]));
            Logger.log(String.format("CPos: %s %s %s", cpos[0], cpos[1], cpos[2]));
            
            ts.mesh = engine.getMesh();
            ts.width = this.getWidth();
            ts.height = this.getHeight();
            
            SunflowRenderer.render(ts);
        }
        repaint();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
