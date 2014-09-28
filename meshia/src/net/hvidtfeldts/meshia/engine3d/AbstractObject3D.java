package net.hvidtfeldts.meshia.engine3d;

import javax.media.opengl.GL2ES2;

import net.hvidtfeldts.utils.EventSource;
import net.hvidtfeldts.utils.EventSource.EventListener;

import com.jogamp.opengl.util.glsl.ShaderState;

public abstract class AbstractObject3D implements Object3D {
    private boolean isInitialized;
    protected final ShaderState shaderState;
    private final String name;
    private boolean isVisible = true;
    private final EventSource<Object3DEvent> eventSource = new EventSource<>();
    
    protected AbstractObject3D(ShaderState shaderState, String name) {
        this.shaderState = shaderState;
        this.name = name;
    }
    
    @Override
    public void addEventListener(EventListener<? super Object3DEvent> listener) {
        eventSource.addEventListener(listener);
    }
    
    @Override
    public void removeEventListener(EventListener<? super Object3DEvent> listener) {
        eventSource.removeEventListener(listener);
    }
    
    @Override
    public boolean isVisible() {
        return isVisible;
    }
    
    @Override
    public void setVisible(boolean value) {
        if (isVisible != value) {
            eventSource.emitEvent(new Object3DVisibleChangedEvent(this));
        }
        isVisible = value;
        
    }
    
    @Override
    public final void init(GL2ES2 gl) {
        isInitialized = true;
        internalInit(gl);
    }
    
    @Override
    public final void draw(GL2ES2 gl) {
        if (!isInitialized) {
            init(gl);
        }
        internalDraw(gl);
    }
    
    public abstract void internalInit(GL2ES2 gl);
    
    public abstract void internalDraw(GL2ES2 gl);
    
    @Override
    public String toString() {
        return name;
    }
}
