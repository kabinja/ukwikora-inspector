package org.ukwikora.inspector.configuration;

import org.junit.jupiter.api.Test;
import org.ukwikora.inspector.Globals;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {
    @Test
    void checkInitializationWithGitlab(){
        File configFile = Globals.getResourceFile("config/config-gitlab.json");

        try {
            Configuration.initialize(configFile.getAbsolutePath());
            Configuration configuration = Configuration.getInstance();

            assertTrue(configuration.isGitLab());
            assertFalse(configuration.isLocalSource());

            assertFalse(configuration.getVerbose());
            assertEquals("INFO", configuration.getLoggerLevel());
            assertEquals("https://url.to.git", configuration.getGitlab().getUrl());
            assertEquals("/path/to/output", configuration.getOutputDirectory());

            assertEquals("RION0947UVP98F", configuration.getGitlab().getToken());
            assertEquals("group I am targetting", configuration.getGitlab().getGroup());
            assertEquals("some branch", configuration.getGitlab().getDefaultBranch());
            assertEquals(1, configuration.getGitlab().getBranchExceptions().size());
            assertEquals("other branch", configuration.getGitlab().getBranchExceptions().get("some project"));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    void checkInitializationWithLocalSource(){
        File configFile = Globals.getResourceFile("config/config-local-sources.json");

        try {
            Configuration.initialize(configFile.getAbsolutePath());
            Configuration configuration = Configuration.getInstance();

            assertFalse(configuration.isGitLab());
            assertTrue(configuration.isLocalSource());

            assertFalse(configuration.getVerbose());
            assertEquals("INFO", configuration.getLoggerLevel());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}