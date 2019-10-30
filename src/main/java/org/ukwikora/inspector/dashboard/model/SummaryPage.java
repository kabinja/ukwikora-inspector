package org.ukwikora.inspector.dashboard.model;

import org.ukwikora.analytics.Clone;
import org.ukwikora.analytics.Clones;
import org.ukwikora.model.Project;
import org.ukwikora.model.UserKeyword;
import org.ukwikora.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SummaryPage extends Page {
    private BarChart deadCodeChart;
    private BarChart duplicatedChart;
    private BarChart userKeywordsChart;
    private BarChart testCasesChart;
    private BarChart cloneChart;
    private List<String> scripts;

    private int linesOfCode;
    private int numberKeywords;
    private int numberTestCases;

    public SummaryPage(String id, String name, Set<Project> projects, Clones<UserKeyword> clones) throws Exception {
        super(id, name);

        linesOfCode = 0;
        numberKeywords = 0;
        numberTestCases = 0;

        int size = projects.size();

        List<String> labels = new ArrayList<>(size);
        List<Integer> executedLines = new ArrayList<>(size);
        List<Integer> deadLines = new ArrayList<>(size);
        List<Integer> onceLines = new ArrayList<>(size);
        List<Integer> duplicatedLines = new ArrayList<>(size);
        List<Integer> userKeywords = new ArrayList<>(size);
        List<Integer> testCases = new ArrayList<>(size);

        Set<UserKeyword> keywords = new HashSet<>();

        for(Project project: projects){
            linesOfCode += project.getLoc();
            numberKeywords += project.getUserKeywords().size();
            numberTestCases += project.getTestCases().size();

            String projectName = project.getName();

            labels.add(StringUtils.toBeautifulName(projectName));
            userKeywords.add(project.getUserKeywords().size());
            testCases.add(project.getTestCases().size());

            int deadLoc = project.getDeadLoc();
            int executedLoc = project.getLoc() - deadLoc;

            executedLines.add(executedLoc);
            deadLines.add(deadLoc);

            int duplicatedLoc = getDuplicatedLines(project, clones);
            int onceLoc = project.getLoc() - duplicatedLoc;

            onceLines.add(onceLoc);
            duplicatedLines.add(duplicatedLoc);

            keywords.addAll(project.getUserKeywords());
        }

        createLinesChart(labels, executedLines, deadLines);
        createDuplicatedChart(labels, onceLines, duplicatedLines);
        createUserKeywordsChart(labels, userKeywords);
        createTestCasesChart(labels, testCases);
        createCloneChart(keywords, clones);

        setChartsHeight();

        scripts = new ArrayList<>(5);
        scripts.add(deadCodeChart.getUrl());
        scripts.add(duplicatedChart.getUrl());
        scripts.add(userKeywordsChart.getUrl());
        scripts.add(testCasesChart.getUrl());
        scripts.add(cloneChart.getUrl());
    }

    private void createTestCasesChart(List<String> labels, List<Integer> testCases) throws IOException {
        ChartDataset dataset = new ChartDataset("Number of Test Cases", testCases, ChartDataset.Color.BLUE);

        testCasesChart = new BarChart(
                "summary-test-cases-chart",
                "Test Cases",
                dataset,
                labels);

        testCasesChart.setYLabel("Number of Test Cases");
    }

    private void createUserKeywordsChart(List<String> labels, List<Integer> userKeywords) throws IOException {
        ChartDataset dataset = new ChartDataset("Number of User Keywords", userKeywords, ChartDataset.Color.BLUE);

        userKeywordsChart = new BarChart(
                "summary-user-keywords-chart",
                "User Keywords",
                dataset,
                labels);

        userKeywordsChart.setYLabel("Number of User Keywords");
    }

    private void createLinesChart(List<String> labels, List<Integer> lines, List<Integer> deadLines) throws IOException {
        List<ChartDataset> dataSets = new ArrayList<>(2);
        dataSets.add(new ChartDataset("Dead", deadLines, ChartDataset.Color.RED));
        dataSets.add(new ChartDataset("Executed", lines, ChartDataset.Color.BLUE));

        deadCodeChart = new BarChart(
                "summary-dead-code-chart",
                "Lines of Code - Dead Code",
                dataSets,
                labels);

        deadCodeChart.setYLabel("Number Lines of Code");
    }

    private void createDuplicatedChart(List<String> labels, List<Integer> lines, List<Integer> duplicated) throws IOException {
        List<ChartDataset> dataSets = new ArrayList<>(2);
        dataSets.add(new ChartDataset("Duplicated", duplicated, ChartDataset.Color.RED));
        dataSets.add(new ChartDataset("Once", lines, ChartDataset.Color.BLUE));

        duplicatedChart = new BarChart(
                "summary-duplicated-chart",
                "Lines of Code - Duplicated",
                dataSets,
                labels);

        duplicatedChart.setYLabel("Number Lines of Code");
    }

    private void createCloneChart(Set<UserKeyword> keywords, Clones<UserKeyword> clones) throws IOException {
        cloneChart = CloneChart.create("", keywords, clones);
    }

    public BarChart getDeadCodeChart() {
        return deadCodeChart;
    }

    public BarChart getDuplicatedChart() {
        return duplicatedChart;
    }

    public BarChart getUserKeywordsChart() {
        return userKeywordsChart;
    }

    public BarChart getTestCasesChart() {
        return testCasesChart;
    }

    public BarChart getCloneChart() {
        return cloneChart;
    }

    public List<String> getScripts() {
        return scripts;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public int getNumberKeywords() {
        return numberKeywords;
    }

    public int getNumberTestCases() {
        return numberTestCases;
    }

    private void setChartsHeight(){
        int height = 600;

        deadCodeChart.setHeight(height);
        duplicatedChart.setHeight(height);
        userKeywordsChart.setHeight(height);
        testCasesChart.setHeight(height);
    }

    private int getDuplicatedLines(Project project, Clones<UserKeyword> clones) {
        int loc = 0;

        for(UserKeyword keyword: project.getUserKeywords()){
            if(clones.getCloneType(keyword) != Clone.Type.None){
                loc += keyword.getLoc();
            }
        }

        return loc;
    }
}
