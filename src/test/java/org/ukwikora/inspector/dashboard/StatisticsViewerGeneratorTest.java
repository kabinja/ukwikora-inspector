package org.ukwikora.inspector.dashboard;

import org.junit.jupiter.api.Test;
import org.ukwikora.inspector.Globals;
import org.ukwikora.model.Project;

import java.io.File;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StatisticsViewerGeneratorTest {
    @Test
    public void checkSiteGeneration(){
        try {
            String[] paths = {
                    "robot/project-suite/project-A",
                    "robot/project-suite/project-B",
                    "robot/project-suite/project-C"
            };

            Set<Project> projects = Globals.compileProjects(paths);
            File destination = Globals.getNewTmpFolder("some folder/static-site");

            StatisticsViewerGenerator generator = new StatisticsViewerGenerator(projects, destination);
            generator.generate();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}