package net.hvidtfeldts.test;

import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.hvidtfeldts.meshia.engine3d.Object3D;
import net.hvidtfeldts.meshia.gui.Project;

import org.junit.Test;
import org.mockito.Mockito;

public class SimpleTest {
    
    @Test
    public void test() {
        Project test = Mockito.mock(Project.class);
        // define return value for method getUniqueId()
        when(test.getObjects()).thenReturn(new ArrayList<Object3D>());
        
        MT.when(test.getObjects());
    }
    
}

class MT {
    public static <T> void when(T method) {
        
    }
}
