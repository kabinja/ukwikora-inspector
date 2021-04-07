package lu.uni.serval.ikora.inspector;

import lu.uni.serval.commons.git.utils.LocalRepository;
import lu.uni.serval.ikora.inspector.configuration.Gitlab;
import lu.uni.serval.ikora.inspector.configuration.InspectorConfiguration;
import lu.uni.serval.ikora.inspector.dashboard.StatisticsViewerGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lu.uni.serval.ikora.core.BuildConfiguration;
import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.commons.git.api.Api;
import lu.uni.serval.commons.git.api.GitEngine;
import lu.uni.serval.commons.git.api.GitEngineFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectAnalyticsCli {
    private static final Logger logger = LogManager.getLogger(ProjectAnalyticsCli.class);

    public void run() throws Exception {
        logger.info("Start analysis...");
        Instant start = Instant.now();

        // read configuration and setup system
        InspectorConfiguration config = InspectorConfiguration.getInstance();
        Set<File> location = getLocation(config);
        File outputDir = new File(config.getOutputDirectory());

        // configuration definition
        BuildConfiguration buildConfiguration = new BuildConfiguration();

        // analyze projects
        logger.info("Start building projects...");
        BuildResult results = Builder.build(location, buildConfiguration, true);

        logger.info(String.format("Projects parsed in %d ms", results.getParsingTime()));
        logger.info(String.format("Projects linked in %d ms", results.getResolveTime()));
        logger.info(String.format("Projects built in %d ms", results.getBuildTime()));

        if(!results.getErrors().isEmpty()){
            logger.warn("Build finish with errors");
        }

        // export to static website
        StatisticsViewerGenerator generator = new StatisticsViewerGenerator(results.getProjects(), outputDir);
        generator.generate();

        long end = Duration.between(start, Instant.now()).toMillis();
        logger.info(String.format("Analysis performed in %d ms", end));
    }

    private Set<File> getLocation(InspectorConfiguration configuration) throws Exception {
        Set<File> location;

        if(configuration.isGitLab()){
            Gitlab gitlabConfig = configuration.getGitlab();

            String tmpFolder = createTmpFolder(gitlabConfig.getLocalFolder());

            final GitEngine git = GitEngineFactory.create(Api.GITLAB);
            git.setToken(gitlabConfig.getToken());
            git.setUrl(gitlabConfig.getUrl());
            git.setCloneFolder(tmpFolder);

            if(gitlabConfig.getDefaultBranch() != null){
                git.setDefaultBranch(gitlabConfig.getDefaultBranch());
            }

            if(gitlabConfig.getBranchExceptions() != null){
                for (Map.Entry<String, String> entry: gitlabConfig.getBranchExceptions().entrySet()){
                    git.setBranchForProject(entry.getKey(), entry.getValue());
                }
            }

            final Set<LocalRepository> localRepos = git.cloneProjectsFromGroup(gitlabConfig.getGroup());
            location = localRepos.stream().map(LocalRepository::getLocation).collect(Collectors.toSet());
        }
        else if(configuration.isLocalSource()){
            location = Collections.emptySet();
        }
        else{
            throw new Exception("Invalid configuration, missing source for text to analyze");
        }

        return location;
    }

    private String createTmpFolder(String location) {
        File folder = location != null ? new File(location) : null;

        if(location == null || location.isEmpty() || !folder.isDirectory()){
            File tmp = FileUtils.getTempDirectory();
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            String folderName = "project-analytics-" + ts.toInstant().toEpochMilli();
            folder = new File(tmp, folderName);

            location = folder.getAbsolutePath();
        }

        if(!folder.exists()) {
            if(!folder.mkdir()){
                logger.error("Failed to create directory '" + location + "'", new IOException("Failed to create directory " + location));
            }
        }

        folder.deleteOnExit();

        return location;
    }
}
