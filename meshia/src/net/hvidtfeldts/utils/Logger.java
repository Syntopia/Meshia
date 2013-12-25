package net.hvidtfeldts.utils;

public class Logger {
    private static Logger log = new Logger();
    
    protected void internalLog(Object obj) {
        System.out.println(obj);
    }
    
    protected void internalWarn(Object obj) {
        System.err.println(obj);
    }
    
    public static void setLogger(Logger newLog) {
        log = newLog;
    }
    
    public static void log(Object obj) {
        log.internalLog(obj);
    }
    
    public static void warn(Object obj) {
        log.internalWarn(obj);
    }
}
