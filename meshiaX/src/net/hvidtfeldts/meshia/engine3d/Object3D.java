package net.hvidtfeldts.meshia.engine3d;

import javax.media.opengl.GL2ES2;

import net.hvidtfeldts.utils.EventSource.Event;
import net.hvidtfeldts.utils.EventSource.EventListener;

public interface Object3D {
    
    void init(GL2ES2 gl);
    
    void draw(GL2ES2 gl);
    
    boolean isVisible();
    
    void setVisible(boolean value);
    
    void addEventListener(EventListener<? super Object3DEvent> listener);
    
    void removeEventListener(EventListener<? super Object3DEvent> listener);
    
    class Object3DEvent implements Event {
        private final Object3D object3D;
        
        Object3DEvent(Object3D object3D) {
            this.object3D = object3D;
        }
        
        public Object3D getObject3D() {
            return object3D;
        }
    };
    
    class Object3DVisibleChangedEvent extends Object3DEvent {
        Object3DVisibleChangedEvent(Object3D object3d) {
            super(object3d);
        }
    };
}