package tech.ikora.inspector.dashboard;

import org.junit.jupiter.api.Test;
import tech.ikora.builder.BuildResult;
import tech.ikora.inspector.Globals;
import tech.ikora.model.Project;

import java.io.File;
import java.util.Set;
import java.util.logging.ErrorManager;

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

            BuildResult build = Globals.compileProjects(paths);
            File destination = Globals.getNewTmpFolder("some folder/static-site");

            assertTrue(build.getErrors().isEmpty());

            StatisticsViewerGenerator generator = new StatisticsViewerGenerator(build.getProjects(), destination);
            generator.generate();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}