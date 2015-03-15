package net.hvidtfeldts.utils;

import java.util.ArrayList;
import java.util.List;

public class EventSource<T> {
    private final List<EventListener<? super T>> listeners = new ArrayList<>();
    
    public void addEventListener(EventListener<? super T> l) {
        listeners.add(l);
    }
    
    public boolean removeEventListener(EventListener<? super T> l) {
        return listeners.remove(l);
    }
    
    public void emitEvent(T event) {
        for (EventListener<? super T> e : listeners) {
            e.eventReceived(event);
        }
    }
    
    public interface EventListener<T> {
        void eventReceived(T event);
    }
    
    public interface Event {
    }
}
