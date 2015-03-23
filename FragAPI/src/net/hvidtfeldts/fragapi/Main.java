package net.hvidtfeldts.fragapi;

public class Main {
    public static void main(final String[] args) {
        Defaults.setResolution(500, 400);
        
        FrameBuffer hdr = FrameBuffer.create3D("RaytracerShader.fp").createFlipFlop("previous");
        
        FrameBuffer outputBuffer = FrameBuffer.create("SimpleShader.fp")
                .setSampler2D("hdrBuffer", hdr)
                .setSampler2D("image", "D:\\HDR\\Paper1.jpg");
        
        outputBuffer.show();
    }
}
