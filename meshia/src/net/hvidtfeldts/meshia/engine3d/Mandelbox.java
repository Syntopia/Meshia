package net.hvidtfeldts.meshia.engine3d;

import net.hvidtfeldts.meshia.math.Vector3;

public class Mandelbox extends SimpleMarchingCubes {
    
    final double Scale = 2.0;
    
    @Override
    protected double getValue(Vector3 p) {
        Vector3 offset = new Vector3(p);
        double dr = 1.0;
        for (int n = 0; n < 8; n++) {
            p.multiply((float) Scale);
            p.add(offset);
            dr = dr * Math.abs(Scale) + 1.0;
        }
        float r = p.getLength();
        return r / Math.abs(dr);
    }
}
