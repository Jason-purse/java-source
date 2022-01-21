package core.lang;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerTests {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(LoggerTests.class.getName());
        logger.setLevel(Level.ALL);

        logger.log(Level.FINE,"1231");
        logger.log(Level.ALL,"1231");
        logger.log(Level.CONFIG,"1231");

    }
}
