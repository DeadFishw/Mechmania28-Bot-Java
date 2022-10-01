package starterpack.util;

import org.apache.logging.log4j.LogManager;
import starterpack.strategy.Strategy;

public class Logger {
    public static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Strategy.class.getName());

}
