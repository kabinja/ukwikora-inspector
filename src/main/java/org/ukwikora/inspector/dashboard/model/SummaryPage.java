package org.ukwikora.inspector.dashboard.model;

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
    private BarChart linesChart;
    private BarChart userKeywordsChart;
    private BarChart testCasesChart;
    private BarChart cloneChart;
    private List<String> scripts;

    private int linesOfCode;
    private int numberKeywords;
    private int numberTestCases;

    public SummaryPage(String id, String name, List<Project> projects, Clones<UserKeyword> clones) throws Exception {
        super(id, name);

        linesOfCode = 0;
        numberKeywords = 0;
        numberTestCases = 0;

        int size = projects.size();

        List<String> labels = new ArrayList<>(size);
        List<Integer> lines = new ArrayList<>(size);
        List<Integer> deadLines = new ArrayList<>(size);
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

            lines.add(executedLoc);
            deadLines.add(deadLoc);

            keywords.addAll(project.getUserKeywords());
        }

        createLinesChart(labels, lines, deadLines);
        createUserKeywordsChart(labels, userKeywords);
        createTestCasesChart(labels, testCases);
        createCloneChart(keywords, clones);

        setChartsHeight();

        scripts = new ArrayList<>(4);
        scripts.add(linesChart.getUrl());
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

        linesChart = new BarChart(
                "summary-lines-of-code-chart",
                "Lines of Code",
                dataSets,
                labels);

        linesChart.setYLabel("Number Lines of Code");
    }

    private void createCloneChart(Set<UserKeyword> keywords, Clones<UserKeyword> clones) throws IOException {
        cloneChart = CloneChart.create("", keywords, clones);
    }

    public BarChart getLinesChart() {
        return linesChart;
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

        linesChart.setHeight(height);
        userKeywordsChart.setHeight(height);
        testCasesChart.setHeight(height);
    }
}
