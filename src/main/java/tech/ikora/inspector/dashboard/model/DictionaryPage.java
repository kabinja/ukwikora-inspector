package tech.ikora.inspector.dashboard.model;

import tech.ikora.model.*;
import tech.ikora.utils.StringUtils;

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
                        testCase.getLibraryName(),
                        testCase.getRange().toString(),
                        projectName
                });
            }

            for(UserKeyword userKeyword: project.getUserKeywords()){
                table.addRow(new String[]{
                        "User Keyword",
                        userKeyword.getName(),
                        userKeyword.getLibraryName(),
                        userKeyword.getRange().toString(),
                        projectName
                });
            }

            for(VariableAssignment variable: project.getVariableAssignments()){
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

    public Table getTable() {
        return table;
    }
}
