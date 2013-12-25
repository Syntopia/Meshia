package net.hvidtfeldts.meshia.engine3d;

import net.hvidtfeldts.meshia.math.Vector3;

public interface Transformator {
    Vector3 transform(Vector3 in);
    
    static class IdentityTransformator implements Transformator {
        private static Transformator instance;
        
        @Override
        public Vector3 transform(Vector3 in) {
            return in;
        }
        
        static Transformator getInstance() {
            if (instance == null) {
                instance = new IdentityTransformator();
            }
            return instance;
        }
    }
}
