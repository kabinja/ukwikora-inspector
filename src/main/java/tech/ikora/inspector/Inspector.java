package tech.ikora.inspector;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.commons.cli.*;
import tech.ikora.inspector.configuration.Configuration;

import java.io.IOException;

public class Inspector {
    public static void main(String[] args) throws Exception {
        int returnValue = 0;

        try {
            initializeLogger();

            Options options = new Options();

            options.addOption("config", true, "path to the json configuration file");

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if(!cmd.hasOption("config")){
                throw new MissingArgumentException("config");
            }

            Configuration.initialize(cmd.getOptionValue("config"));
            Configuration config = Configuration.getInstance();

            setLoggerLevel(config);

            ProjectAnalyticsCli projectAnalyticsCli = new ProjectAnalyticsCli();
            projectAnalyticsCli.run();

        } catch (ParseException e) {
            getLogger().error(String.format("Parse Exception: argument '%s' is missing", e.getMessage()));
            returnValue = -1;
        } catch (IOException e) {
            getLogger().error(String.format("IO Exception: %s", e.getMessage()));
            returnValue = 1;
        }

        getLogger().info("program finished");

        System.exit(returnValue);
    }

    private static void initializeLogger(){
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        AppenderComponentBuilder console = builder.newAppender("stdout", "Console");
        builder.add(console);

        LayoutComponentBuilder standard = builder.newLayout("PatternLayout");
        standard.addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable");

        console.add(standard);

        Configurator.initialize(builder.build());
        Configurator.setLevel("org.reflections", Level.OFF);
    }

    private static void setLoggerLevel(Configuration config){
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration log4jConfig = context.getConfiguration();
        LoggerConfig loggerConfig = log4jConfig.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(getLoggerLevel(config));

        context.updateLoggers();
    }

    private static Logger getLogger(){
        return LogManager.getLogger(Inspector.class);
    }

    private static Level getLoggerLevel(Configuration config){
        String level = config != null ? config.getLoggerLevel().toUpperCase() : "";

        Level loggerLevel;

        switch (level){
            case "ALL": loggerLevel = Level.ALL; break;
            case "TRACE": loggerLevel = Level.TRACE; break;
            case "DEBUG": loggerLevel = Level.DEBUG; break;
            case "WARN": loggerLevel = Level.WARN; break;
            case "ERROR": loggerLevel = Level.ERROR; break;
            case "FATAL": loggerLevel = Level.FATAL; break;
            case "OFF": loggerLevel = Level.OFF; break;
            case "INFO":
            default: loggerLevel = Level.INFO; break;
        }

        return loggerLevel;
    }
}
