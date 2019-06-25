package org.ukwikora.inspector.dashboard.model;

import org.ukwikora.model.Project;
import org.ukwikora.model.UserKeyword;
import org.ukwikora.model.Variable;
import org.ukwikora.utils.StringUtils;

import java.util.List;

public class DeadCodePage extends Page {

    private final Table table;

    public DeadCodePage(String id, String name, List<Project> projects) throws Exception {
        super(id, name);

        this.table = new Table(
                id,
                "Dead Code",
                new String[]{"Type", "Name", "File", "Lines", "Project"}
        );

        for(Project project: projects){
            String projectName = StringUtils.toBeautifulName(project.getName());

            for(UserKeyword userKeyword: project.getUserKeywords()){
                if(userKeyword.getDependencies().isEmpty()){
                    table.addRow(new String[]{
                            "User Keyword",
                            userKeyword.getName(),
                            userKeyword.getFileName(),
                            userKeyword.getLineRange().toString(),
                            projectName
                    });
                }
            }

            for(Variable variable: project.getVariables()){
                if(variable.getDependencies().isEmpty()){
                    table.addRow(new String[]{
                            "Variable",
                            variable.getName(),
                            variable.getFileName(),
                            variable.getLineRange().toString(),
                            projectName
                    });
                }
            }
        }
    }

    public Table getTable() {
        return table;
    }
}
