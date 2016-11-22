package de.derpeterson.webapp.helper;

import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum LogHelper {

	INSTANCE;
	
	private LogHelper() {}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LogHelper.class);

    public void tieSystemOutAndErrToLog() {
        System.setOut(createOutLoggingProxy(System.out));
        System.setErr(createErrLoggingProxy(System.err));
    }

    public PrintStream createOutLoggingProxy(final PrintStream realPrintStream) {
        return new PrintStream(realPrintStream) {
            public void print(final String string) {
                realPrintStream.print(string);
                LOGGER.info(string);
            }
        };
    }
    
    public  PrintStream createErrLoggingProxy(final PrintStream realPrintStream) {
        return new PrintStream(realPrintStream) {
            public void print(final String string) {
                realPrintStream.print(string);
                LOGGER.error(string);
            }
        };
    }
}
