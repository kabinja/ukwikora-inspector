package lu.uni.serval.ikora.inspector.dashboard.model;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.StringUtils;

public class DeadCodePage extends Page {

    private final Table table;

    public DeadCodePage(String id, String name, Projects projects) throws Exception {
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
                            userKeyword.getName(),
                            userKeyword.getLibraryName(),
                            userKeyword.getRange().toString(),
                            projectName
                    });
                }
            }

            for(VariableAssignment variable: project.getVariableAssignments()){
                if(variable.isDeadCode()){
                    table.addRow(new String[]{
                            "Variable",
                            variable.getName(),
                            variable.getLibraryName(),
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
