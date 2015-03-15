package net.hvidtfeldts.fragapi;

public final class Defaults {
    private static int x = 400;
    private static int y = 300;
    private static boolean measureFPS;
    
    private Defaults() {
    };
    
    public static void setResolution(int x, int y) {
        Defaults.x = x;
        Defaults.y = y;
    }
    
    public static double getWidth() {
        return x;
    }
    
    public static double getHeight() {
        return y;
    }
    
    public static boolean getMeasureFPS() {
        return measureFPS;
    }
    
    public static void setMeasureFPS(boolean value) {
        measureFPS = value;
    }
}
