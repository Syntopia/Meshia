package net.hvidtfeldts.meshia.engine3d;

import net.hvidtfeldts.meshia.math.Vector3;

public class Mandelbulb extends SimpleMarchingCubes {
    
    @Override
    protected double getValue(Vector3 p) {
        double power = 6;
        p.multiply(1.15f);
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
        
        double r = 0;
        double dr = 1;
        
        for (int j = 0; j < 10; j++) {
            if (x * x + y * y + z * z > 10000)
                break;
            
            double phi = Math.atan2(y, x);
            r = Math.sqrt(x * x + y * y + z * z);
            double theta = Math.acos(z / r);
            dr = Math.pow(r, power - 1.0) * power * dr + 1.0;
            r = Math.pow(r, power);
            double sin = Math.sin(power * theta);
            x = r * sin * Math.cos(power * phi);
            y = r * sin * Math.sin(power * phi);
            z = r * Math.cos(power * theta);
            
            x += p.getX();
            y += p.getY();
            z += p.getZ();
        }
        double d = (0.5 * Math.log(r) * r / dr) - 0.0019;
        return d;
    }
    
}
