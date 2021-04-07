package lu.uni.serval.ikora.inspector.dashboard;

import lu.uni.serval.ikora.inspector.Globals;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.core.builder.BuildResult;

import java.io.File;

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