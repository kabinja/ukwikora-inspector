package tech.ikora.inspector.dashboard.model;

import tech.ikora.inspector.utils.JsonUtils;

import java.io.IOException;
import java.util.Set;

public class DependencyGraph extends Chart{
    final private Set<Dependency> dependencies;

    protected DependencyGraph(String id, String name, Set<Dependency> dependencies) {
        super(id, name);
        this.dependencies = dependencies;
    }

    public String getJsonDependencies() throws IOException {
        return JsonUtils.convertToJsonArray(this.dependencies);
    }
}
