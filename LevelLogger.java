/*
 * File: LevelLogger.java
 * Author: David Hui
 * Description: Separates logging into different channels
 */
public class LevelLogger{
    public static final int DEBUG = 4;
    public static final int INFO = 3;
    public static final int WARN = 2;
    public static final int ERR = 1;
    public static final int CRIT = 0;
    
    private static int logLevel = INFO;
    private static int defaultLevel = INFO;
    
    public static void setLogLevel(int nlogLevel){
        logLevel = nlogLevel;
    }
    
    public static void setDefaultLogLevel(int ndefaultLevel){
        defaultLevel = ndefaultLevel;
    }
    
    public static void log(String log, int nlogLevel){
        if(logLevel >= nlogLevel){
            System.out.println(log);
        }
    }
    
    public static void log(Object log, int nlogLevel){
        log(log.toString(), nlogLevel);
    }
    
    public static void log(String log){
        log(log, defaultLevel);
    }
    
    public static void log(Object log){
        log(log, defaultLevel);
    }
    
    
}