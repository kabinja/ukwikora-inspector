package org.ukwikora.inspector;

import org.apache.commons.io.FileUtils;
import org.ukwikora.builder.Builder;
import org.ukwikora.model.Project;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

public class Globals {
    public static File getResourceFile(String name){
        File file;

        try {
            URL resource = Globals.class.getClassLoader().getResource(name);
            if(resource == null) throw new Exception("Null URI returned for resources!");
            file = Paths.get(resource.toURI()).toFile();
        } catch (Exception e) {
            fail(String.format("Failed to load resource '%s': %s", name, e.getMessage()));
            file = null;
        }

        return file;
    }

    public static Set<Project> compileProjects(String[] resourcesPaths){
        Set<File> files = new HashSet<>();

        for(int i = 0; i < resourcesPaths.length; ++i){
            files.add(getResourceFile(resourcesPaths[i]));
        }

        return Builder.build(files, true);
    }

    public static File getNewTmpFolder(String name){
        String tmpPath = System.getProperty("java.io.tmpdir");
        File tmpDir = new File(tmpPath);
        File directory = new File(tmpDir, name);
        deleteDirectory(directory);

        assertFalse(directory.exists());

        return directory;
    }

    public static void deleteDirectory(File directory){
        if(directory.exists()){
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                fail("Failed to clean " + directory.getAbsolutePath());
            }
        }
    }
}
