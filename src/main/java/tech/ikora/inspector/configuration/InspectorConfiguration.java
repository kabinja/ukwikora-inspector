package tech.ikora.inspector.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class InspectorConfiguration {
    private Boolean verbose;
    @JsonProperty("logger level")
    private String loggerLevel;
    @JsonProperty("output directory")
    private String outputDirectory;
    private Gitlab gitlab;
    @JsonProperty("local source")
    private LocalSource localSource;

    @JsonIgnore
    private static InspectorConfiguration instance = new InspectorConfiguration();
    @JsonIgnore
    private static final Logger logger = LogManager.getLogger(InspectorConfiguration.class);

    private InspectorConfiguration(){
        verbose = true;
        loggerLevel = "INFO";
    }

    public static InspectorConfiguration getInstance()
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

    public static void setInstance(InspectorConfiguration instance) {
        InspectorConfiguration.instance = instance;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void initialize(String config) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(config);

        instance = mapper.readValue(file, InspectorConfiguration.class);
        logger.info("Configuration loaded from " + config);
    }

    public boolean isGitLab() {
        return gitlab != null;
    }

    public boolean isLocalSource() {
        return localSource != null;
    }
}
