package tech.ikora.inspector.dashboard.model;

import tech.ikora.model.Project;
import tech.ikora.model.TestCase;
import tech.ikora.model.UserKeyword;
import tech.ikora.model.Variable;
import tech.ikora.utils.StringUtils;

import java.util.Set;

public class DictionaryPage extends Page {

    private final Table table;

    public DictionaryPage(String id, String name, Set<Project> projects) throws Exception {
        super(id, name);

        this.table = new Table(
                id,
                "Dictionary",
                new String[]{"Type", "Name", "File", "Lines", "Project"}
        );

        for(Project project: projects){
            String projectName = StringUtils.toBeautifulName(project.getName());

            for(TestCase testCase: project.getTestCases()){
                table.addRow(new String[]{
                        "Test Case",
                        testCase.getName().toString(),
                        testCase.getFileName(),
                        testCase.getRange().toString(),
                        projectName
                });
            }

            for(UserKeyword userKeyword: project.getUserKeywords()){
                table.addRow(new String[]{
                        "User Keyword",
                        userKeyword.getName().toString(),
                        userKeyword.getFileName(),
                        userKeyword.getRange().toString(),
                        projectName
                });
            }

            for(Variable variable: project.getVariables()){
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

    public Table getTable() {
        return table;
    }
}
