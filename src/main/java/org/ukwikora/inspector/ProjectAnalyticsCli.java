package org.ukwikora.inspector;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.builder.Builder;
import org.ukwikora.gitlabloader.Gitlab;
import org.ukwikora.inspector.configuration.Configuration;
import org.ukwikora.inspector.dashboard.StatisticsViewerGenerator;
import org.ukwikora.model.Project;
import org.ukwikora.utils.CommandRunner;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class ProjectAnalyticsCli implements CommandRunner {
    private static final Logger logger = LogManager.getLogger(ProjectAnalyticsCli.class);

    @Override
    public void run() throws Exception {
        logger.info("Start analysis...");
        Instant start = Instant.now();

        // read configuration and setup system
        Configuration config = Configuration.getInstance();
        String[] location = getLocation(config);
        File outputDir = new File(config.getOutputDirectory());

        // analyze projects
        List<Project> projects = Builder.build(location, true);

        // export to static website
        StatisticsViewerGenerator generator = new StatisticsViewerGenerator(projects, outputDir);
        generator.generate();

        long end = Duration.between(start, Instant.now()).toMillis();
        logger.info(String.format("Analysis performed in %d ms", end));
    }

    private String[] getLocation(Configuration configuration) throws Exception {
        String[] location;

        if(configuration.isGitLab()){
            org.ukwikora.inspector.configuration.Gitlab gitlabConfig = configuration.getGitlab();
            String tmpFolder = createTmpFolder(gitlabConfig.getLocalFolder());

            Gitlab gitlab = new Gitlab().setToken(gitlabConfig.getToken()).setUrl(gitlabConfig.getUrl());
            List<org.ukwikora.gitlabloader.Project> projects = gitlab.findProjectsByGroupName(gitlabConfig.getGroup());
            gitlab.cloneProjects(projects, tmpFolder, gitlabConfig.getDefaultBranch(), gitlabConfig.getBranchExceptions());

            location = org.ukwikora.utils.FileUtils.getSubFolders(tmpFolder);
        }
        else if(configuration.isLocalSource()){
            location = configuration.getLocalSource().getFolders().toArray(new String[0]);
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
