package org.ukwikora.inspector.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class Configuration {
    private Boolean verbose;
    @JsonProperty("logger level")
    private String loggerLevel;
    @JsonProperty("output directory")
    private String outputDirectory;
    private Gitlab gitlab;
    @JsonProperty("local source")
    private LocalSource localSource;

    @JsonIgnore
    private static File configurationFolder;
    @JsonIgnore
    private static Configuration instance = new Configuration();
    @JsonIgnore
    private static final Logger logger = LogManager.getLogger(org.ukwikora.utils.Configuration.class);

    private Configuration(){
        verbose = true;
        loggerLevel = "INFO";
    }

    public static Configuration getInstance()
    {   return instance;
    }

    public Boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(Boolean value) {
        verbose = value;
    }

    public String getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(String value) {
        loggerLevel = value;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory){
        this.outputDirectory = outputDirectory;
    }

    public Gitlab getGitlab(){
        return gitlab;
    }

    public void setGitlab(Gitlab gitlab){
        this.gitlab = gitlab;
    }

    public Boolean getVerbose() {
        return verbose;
    }

    public LocalSource getLocalSource() {
        return localSource;
    }

    public void setLocalSource(LocalSource localSource) {
        this.localSource = localSource;
    }

    public static void setConfigurationFolder(File configurationFolder) {
        Configuration.configurationFolder = configurationFolder;
    }

    public static void setInstance(Configuration instance) {
        Configuration.instance = instance;
    }

    public static Logger getLogger() {
        return logger;
    }

    public File getConfigurationFolder(){
        return configurationFolder;
    }

    public static void initialize(String config) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(config);

        instance = mapper.readValue(file, Configuration.class);

        AppDirs appDirs = AppDirsFactory.getInstance();
        String configPath = appDirs.getUserDataDir("ukwikora-inspector", "0.0.1", "kabinja");

        configurationFolder = new File(configPath);
        configurationFolder.mkdirs();

        logger.info("Configuration loaded from " + config);
    }

    public boolean isGitLab() {
        return gitlab != null;
    }

    public boolean isLocalSource() {
        return localSource != null;
    }
}
