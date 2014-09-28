package net.hvidtfeldts.meshia.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import net.hvidtfeldts.meshia.engine3d.Object3D;
import net.hvidtfeldts.meshia.engine3d.Object3D.Object3DEvent;
import net.hvidtfeldts.meshia.engine3d.Object3D.Object3DVisibleChangedEvent;
import net.hvidtfeldts.utils.EventSource;
import net.hvidtfeldts.utils.EventSource.Event;
import net.hvidtfeldts.utils.EventSource.EventListener;

public class Project implements ListModel<Object3D>, EventListener<Object3DEvent> {
    private final List<Object3D> objects = new ArrayList<>();
    private final EventSource<ProjectEvent> eventSource = new EventSource<>();
    private final List<ListDataListener> listeners = new ArrayList<ListDataListener>();
    
    public void addEventListener(EventListener<ProjectEvent> listener) {
        eventSource.addEventListener(listener);
    }
    
    public void addObject(Object3D object) {
        objects.add(object);
        eventSource.emitEvent(new ProjectItemAddedEvent(object));
        
        int id = objects.size() - 1;
        for (ListDataListener l : listeners) {
            l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, id, id));
        }
        object.addEventListener(this);
    }
    
    public void removeObject(Object3D object) {
        int id = objects.indexOf(object);
        objects.remove(object);
        eventSource.emitEvent(new ProjectItemDeletedEvent(object));
        for (ListDataListener l : listeners) {
            l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, id, id));
        }
        object.removeEventListener(this);
    }
    
    public interface ProjectEvent extends Event {
    }
    
    public class ProjectItemVisibilityChangedEvent implements ProjectEvent {
        Object3D object;
        
        public ProjectItemVisibilityChangedEvent(Object3D object) {
            super();
            this.object = object;
        }
        
        public Object3D getObject() {
            return object;
        }
    }
    
    public class ProjectItemAddedEvent implements ProjectEvent {
        Object3D object;
        
        public ProjectItemAddedEvent(Object3D object) {
            super();
            this.object = object;
        }
        
        public Object3D getObject() {
            return object;
        }
    }
    
    public class ProjectItemDeletedEvent implements ProjectEvent {
        Object3D object;
        
        public ProjectItemDeletedEvent(Object3D object) {
            super();
            this.object = object;
        }
        
        public Object3D getObject() {
            return object;
        }
    }
    
    @Override
    public int getSize() {
        return objects.size();
    }
    
    @Override
    public Object3D getElementAt(int index) {
        return objects.get(index);
    }
    
    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }
    
    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
    
    public List<Object3D> getObjects() {
        return objects;
        
    }
    
    @Override
    public void eventReceived(Object3DEvent event) {
        if (event instanceof Object3D.Object3DVisibleChangedEvent) {
            Object3DVisibleChangedEvent e = (Object3DVisibleChangedEvent) event;
            eventSource.emitEvent(new ProjectItemVisibilityChangedEvent(e.getObject3D()));
        }
    }
}
