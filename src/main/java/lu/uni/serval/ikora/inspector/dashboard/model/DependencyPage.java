package lu.uni.serval.ikora.inspector.dashboard.model;

import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.Projects;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class DependencyPage extends Page {
    private final DependencyGraph dependencyGraph;

    public DependencyPage(String id, String name, Projects projects) {
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
