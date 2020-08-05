package tech.ikora.inspector.dashboard.model;

import tech.ikora.model.Project;
import tech.ikora.model.Projects;
import tech.ikora.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SideBar {
    private final List<Link> links;
    private final List<String> labels;

    public SideBar(Projects projects) throws Exception {
        int size = projects.size();
        labels = new ArrayList<>(size);
        links = new ArrayList<>(size);

        for(Project project: projects){
            String name = project.getName();
            links.add(new Link(name));
            labels.add(StringUtils.toBeautifulName(name));
        }
    }

    public List<Link> getLinks(){
        return links;
    }

    public List<String> getLabels() {
        return labels;
    }
}
