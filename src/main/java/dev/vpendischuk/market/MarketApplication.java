package dev.vpendischuk.market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Properties;

/**
 * The application entry point class.
 */
@SpringBootApplication
public class MarketApplication {
    /* ---------------------------- Static fields -------------------------- */

    /**
     * {@link MarketApplication} class-level logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(MarketApplication.class);

    /* -------------------------- Public methods -------------------------- */

    /**
     * The application entry point method.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        Properties properties = new Properties();

        // Configuring logging from command line arguments.
        configureLogging(properties, args);

        new SpringApplicationBuilder(MarketApplication.class).properties(properties).run(args);
    }

    /* -------------------------- Private methods -------------------------- */

    /**
     * Enables logging to the .txt or .log file in the command line arguments.
     * <p>
     * Note: if multiple files with .txt or .log file extensions are specified,
     *   only the last (existing) file will be used for logging.
     *
     * @param properties application properties.
     * @param args command line arguments.
     */
    private static void configureLogging(Properties properties, String[] args) {
        int logCount = 0;
        for (String arg : args) {
            if (arg.endsWith(".txt") || arg.endsWith(".log")) {
                logCount++;
                properties.put("logging.file.name", arg);
            }
        }

        if (logCount == 0) {
            logger.info("No log file path specified - writing logs to standard output only");
        }
    }
}
