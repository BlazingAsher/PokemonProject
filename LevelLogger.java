/*
 * File: LevelLogger.java
 * Author: David Hui
 * Description: Separates logging into different channels
 */
public class LevelLogger{
    public static final int DEBUG = 4; // DEBUG level constant
    public static final int INFO = 3; // INFO level constant
    public static final int WARN = 2; // WARN level constant
    public static final int ERR = 1; // ERR level constant
    public static final int CRIT = 0; // CRIT level constant
    
    private static int logLevel = INFO; // Set default display level to INFO
    private static int defaultLevel = INFO; // Set default logging level to INFO

    /**
     * Sets the display log level
     * @param nlogLevel The maximum log level to display
     */
    public static void setLogLevel(int nlogLevel){
        logLevel = nlogLevel;
    }

    /**
     * Sets the default logging level
     * @param ndefaultLevel The default logging level
     */
    public static void setDefaultLogLevel(int ndefaultLevel){
        defaultLevel = ndefaultLevel;
    }

    /**
     * Log something
     * @param log The String to log
     * @param nlogLevel The log level that the log is
     */
    public static void log(String log, int nlogLevel){
        if(logLevel >= nlogLevel){ // Only log if it is lower or equal to the current display log level
            System.out.println(log);
        }
    }

    /**
     * Log something
     * @param log The Object to log
     * @param nlogLevel The log level that the log is
     * @see #log(String, int)
     */
    public static void log(Object log, int nlogLevel){
        log(log.toString(), nlogLevel);
    }

    /**
     * Log something
     * @param log The String to log
     * @see #log(String, int)
     */
    public static void log(String log){
        log(log, defaultLevel);
    }

    /**
     * Log something
     * @param log The Object to log
     * @see #log(String, int)
     */
    public static void log(Object log){
        log(log, defaultLevel);
    }
    
    
}