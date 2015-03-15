package net.hvidtfeldts.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.SwingUtilities;

public class Logger {
    private static Logger log = new Logger();
    private static OutputStream stream = new LoggerPrintStream();
    private final Deque<Long> timeStack = new ArrayDeque<Long>();
    
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
    
    public static PrintStream getLoggerWarnStream() {
        return new PrintStream(stream);
    }
    
    public static void startTime() {
        log.internalStartTime();
    }
    
    private void internalStartTime() {
        timeStack.push(System.currentTimeMillis());
    }
    
    public static void endTime(String message) {
        log.internalEndTime(message);
    }
    
    private void internalEndTime(String message) {
        Long millis = System.currentTimeMillis() - timeStack.pop();
        log.internalLog(String.format("[%s ms] %s", millis, message));
    }
    
    private static class LoggerPrintStream extends OutputStream {
        
        private final StringBuffer sb = new StringBuffer();
        
        @Override
        public void flush() {
        }
        
        @Override
        public void close() {
        }
        
        @Override
        public void write(int b) throws IOException {
            
            if (b == '\r')
                return;
            
            if (b == '\n') {
                final String text = sb.toString() + "\n";
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        warn(text);
                    }
                });
                sb.setLength(0);
                return;
            }
            
            sb.append((char) b);
        }
    }
    
}
