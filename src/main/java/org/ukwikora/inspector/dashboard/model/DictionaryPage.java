package org.ukwikora.inspector.dashboard.model;

import org.ukwikora.model.Project;
import org.ukwikora.model.TestCase;
import org.ukwikora.model.UserKeyword;
import org.ukwikora.model.Variable;
import org.ukwikora.utils.StringUtils;

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
                        testCase.getName(),
                        testCase.getFileName(),
                        testCase.getLineRange().toString(),
                        projectName
                });
            }

            for(UserKeyword userKeyword: project.getUserKeywords()){
                table.addRow(new String[]{
                        "User Keyword",
                        userKeyword.getName(),
                        userKeyword.getFileName(),
                        userKeyword.getLineRange().toString(),
                        projectName
                });
            }

            for(Variable variable: project.getVariables()){
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

    public Table getTable() {
        return table;
    }
}
