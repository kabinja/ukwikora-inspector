package tech.ikora.inspector.dashboard.model;

import tech.ikora.model.Project;
import tech.ikora.model.UserKeyword;
import tech.ikora.model.Variable;
import tech.ikora.utils.StringUtils;

import java.util.Set;

public class DeadCodePage extends Page {

    private final Table table;

    public DeadCodePage(String id, String name, Set<Project> projects) throws Exception {
        super(id, name);

        this.table = new Table(
                id,
                "Dead Code",
                new String[]{"Type", "Name", "File", "Lines", "Project"}
        );

        for(Project project: projects){
            String projectName = StringUtils.toBeautifulName(project.getName());

            for(UserKeyword userKeyword: project.getUserKeywords()){
                if(userKeyword.isDeadCode()){
                    table.addRow(new String[]{
                            "User Keyword",
                            userKeyword.getName().toString(),
                            userKeyword.getFileName(),
                            userKeyword.getRange().toString(),
                            projectName
                    });
                }
            }

            for(Variable variable: project.getVariables()){
                if(variable.isDeadCode()){
                    table.addRow(new String[]{
                            "Variable",
                            variable.getName().toString(),
                            variable.getFileName(),
                            variable.getRange().toString(),
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
