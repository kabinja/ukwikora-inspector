package org.ukwikora.inspector;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.builder.Builder;
import org.ukwikora.gitloader.Api;
import org.ukwikora.gitloader.GitEngine;
import org.ukwikora.gitloader.GitEngineFactory;
import org.ukwikora.gitloader.git.LocalRepo;
import org.ukwikora.inspector.configuration.Configuration;
import org.ukwikora.inspector.configuration.Gitlab;
import org.ukwikora.inspector.dashboard.StatisticsViewerGenerator;
import org.ukwikora.model.Project;
import org.ukwikora.utils.CommandRunner;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectAnalyticsCli implements CommandRunner {
    private static final Logger logger = LogManager.getLogger(ProjectAnalyticsCli.class);

    @Override
    public void run() throws Exception {
        logger.info("Start analysis...");
        Instant start = Instant.now();

        // read configuration and setup system
        Configuration config = Configuration.getInstance();
        Set<File> location = getLocation(config);
        File outputDir = new File(config.getOutputDirectory());

        // analyze projects
        Set<Project> projects = Builder.build(location, true);

        // export to static website
        StatisticsViewerGenerator generator = new StatisticsViewerGenerator(projects, outputDir);
        generator.generate();

        long end = Duration.between(start, Instant.now()).toMillis();
        logger.info(String.format("Analysis performed in %d ms", end));
    }

    private Set<File> getLocation(Configuration configuration) throws Exception {
        Set<File> location;

        if(configuration.isGitLab()){
            Gitlab gitlabConfig = configuration.getGitlab();

            String tmpFolder = createTmpFolder(gitlabConfig.getLocalFolder());

            final GitEngine git = GitEngineFactory.create(Api.Gitlab);
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

            final Set<LocalRepo> localRepos = git.cloneProjectsFromGroup(gitlabConfig.getGroup());
            location = localRepos.stream().map(LocalRepo::getLocation).collect(Collectors.toSet());
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
