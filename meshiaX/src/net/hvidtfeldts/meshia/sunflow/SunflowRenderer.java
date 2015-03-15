package net.hvidtfeldts.meshia.sunflow;

import javax.swing.JDialog;

import org.sunflow.SunflowAPI;
import org.sunflow.system.ImagePanel;

public class SunflowRenderer {
    public static void render(final TestScene ts) {
        
        JDialog jd = new JDialog();
        
        final ImagePanel ip = new ImagePanel();
        ip.setVisible(true);
        
        jd.add(ip);
        jd.setSize(640, 480);
        jd.setVisible(true);
        
        new Thread() {
            @Override
            public void run() {
                ts.build();
                ts.parameter("sampler", "ipr");
                ts.options(SunflowAPI.DEFAULT_OPTIONS);
                ts.render(SunflowAPI.DEFAULT_OPTIONS, ip);
            }
            
        }.start();
        
    }
}