package org.ukwikora.inspector.dashboard.model;

import org.ukwikora.model.Project;
import org.ukwikora.utils.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DependencyPage extends Page {
    private final DependencyGraph dependencyGraph;

    public DependencyPage(String id, String name, Set<Project> projects) {
        super(id, name);

        Set<Dependency> dependencies = new HashSet<>();

        for(Project target: projects){
            for(Project source: target.getDependencies()){
                String sourceName = StringUtils.toBeautifulName(source.getName());
                String targetName = StringUtils.toBeautifulName(target.getName());
                dependencies.add(new Dependency(sourceName, targetName, Dependency.Type.UserProject));
            }
        }

        this.dependencyGraph = new DependencyGraph(id, name, dependencies);
    }

    public DependencyGraph getGraph() {
        return this.dependencyGraph;
    }
}
