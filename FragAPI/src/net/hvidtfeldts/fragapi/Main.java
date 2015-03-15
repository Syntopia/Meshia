package net.hvidtfeldts.fragapi;

public class Main {
    private Main() {
    }
    
    public static void main(final String[] args) {
        FrameBuffer fp = new FrameBuffer()
                .setFragmentShader(Files.read("RaytracerShader.fp"))
                .setVertexShader(Files.read("RaytracerShader.vp"));
        
        FrameBuffer outputBuffer = new FrameBuffer()
                .setFragmentShader(Files.read("SimpleShader.fp"))
                .setVertexShader(Files.read("SimpleShader.vp"))
                .setSampler2D(fp, "bufferx");
        
        FrameBufferWindow.show(outputBuffer);
    }
}
