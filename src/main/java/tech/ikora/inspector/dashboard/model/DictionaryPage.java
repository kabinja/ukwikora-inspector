package tech.ikora.inspector.dashboard.model;

import tech.ikora.model.*;
import tech.ikora.utils.StringUtils;

import java.util.Set;

public class DictionaryPage extends Page {

    private final Table table;

    public DictionaryPage(String id, String name, Projects projects) throws Exception {
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
                        testCase.getName(),
                        testCase.getSourceFile().getName(),
                        testCase.getRange().toString(),
                        projectName
                });
            }

            for(UserKeyword userKeyword: project.getUserKeywords()){
                table.addRow(new String[]{
                        "User Keyword",
                        userKeyword.getName(),
                        userKeyword.getSourceFile().getName(),
                        userKeyword.getRange().toString(),
                        projectName
                });
            }

            for(VariableAssignment variable: project.getVariables()){
                table.addRow(new String[]{
                        "Variable",
                        variable.getName(),
                        variable.getSourceFile().getName(),
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
