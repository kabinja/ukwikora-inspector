package lu.uni.serval.ikora.inspector.dashboard.model;

import lu.uni.serval.ikora.core.analytics.ProjectStatistics;
import lu.uni.serval.ikora.core.analytics.clones.Clones;
import lu.uni.serval.ikora.core.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.UserKeyword;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.io.IOException;
import java.util.*;

public class SingleProjectPage extends Page {
    private final Link link;
    private final List<String> scripts;

    private final int numberKeywords;
    private final int numberTestCases;
    private final int linesOfCode;

    private final BarChart sizeChart;
    private final BarChart connectivityChart;
    private final BarChart depthChart;
    private final BarChart sequenceChart;
    private final DependencyGraph dependencyGraph;
    private final BarChart cloneChart;

    public SingleProjectPage(Project project, Clones<KeywordDefinition> clones) throws Exception {
        super(
            StringUtils.toBeautifulUrl(project.getName(), ""),
            StringUtils.toBeautifulName(project.getName())
         );

        this.scripts = new ArrayList<>();
        this.link = new Link(project.getName());

        ProjectStatistics statistics = new ProjectStatistics(project);

        this.numberKeywords = statistics.getNumberKeywords(UserKeyword.class);
        this.numberTestCases = statistics.getNumberKeywords(TestCase.class);
        this.linesOfCode = statistics.getLoc();

        this.sizeChart = createSizeChart(statistics);
        this.connectivityChart = createConnectivityChart(statistics);
        this.depthChart = createDepthChart(statistics);
        this.sequenceChart = createSequenceChart(statistics);
        this.dependencyGraph = createDependencyGraph(project);
        this.cloneChart = createCloneChart(project, clones);
    }

    private BarChart createConnectivityChart(ProjectStatistics statistics) throws IOException, InvalidTypeException {
        Map<Integer, Integer> connectivity = statistics.getConnectivityDistribution(UserKeyword.class);

        ChartDataset dataset = new ChartDataset("Number of Keywords", getValues(connectivity), ChartDataset.Color.BLUE);

        BarChart chart = new BarChart(
                String.format("%s-connectivity-chart", getId()),
                "Keywords Connectivity",
                dataset,
                getKeys(connectivity));

        chart.setYLabel("Number of Keywords");
        chart.setXLabel("Number of keywords depending on the keyword");

        this.scripts.add(chart.getUrl());

        return chart;
    }

    private BarChart createDepthChart(ProjectStatistics statistics) throws IOException, InvalidTypeException {
        Map<Integer, Integer> depth = statistics.getLevelDistribution(UserKeyword.class);

        ChartDataset dataset = new ChartDataset("Number of Keywords", getValues(depth), ChartDataset.Color.BLUE);

        BarChart chart = new BarChart(
                String.format("%s-depth-chart", getId()),
                "Keywords Depth",
                dataset,
                getKeys(depth));

        chart.setYLabel("Number of Keywords");
        chart.setXLabel("Longest distance to a library keyword");

        this.scripts.add(chart.getUrl());

        return chart;
    }

    private BarChart createSequenceChart(ProjectStatistics statistics) throws IOException, InvalidTypeException {
        Map<Integer, Integer> sequence = statistics.getSequenceDistribution(TestCase.class);

        ChartDataset dataset = new ChartDataset("Number of Test Cases", getValues(sequence), ChartDataset.Color.BLUE);

        BarChart chart = new BarChart(
                String.format("%s-sequence-chart", getId()),
                "Test Cases Sequence",
                dataset,
                getKeys(sequence));

        chart.setYLabel("Number of Test Cases");
        chart.setXLabel("Number of actions performed");

        this.scripts.add(chart.getUrl());

        return chart;
    }

    private BarChart createSizeChart(ProjectStatistics statistics) throws IOException, InvalidTypeException {
        Map<Integer, Integer> size = statistics.getSizeDistribution(UserKeyword.class);

        ChartDataset dataset = new ChartDataset("Number of Keywords", getValues(size), ChartDataset.Color.BLUE);

        BarChart chart = new BarChart(
                String.format("%s-size-chart", getId()),
                "Keywords Size",
                dataset,
                getKeys(size));

        chart.setYLabel("Number of Keywords");
        chart.setXLabel("Number of keywords composing the keyword");

        this.scripts.add(chart.getUrl());

        return chart;
    }

    private DependencyGraph createDependencyGraph(Project project) {
        Set<Dependency> dependencies = new HashSet<>();

        getDependencies(project, dependencies, new HashSet<>());

        DependencyGraph chart = new DependencyGraph(
                String.format("%s-dependency-graph", getId()),
                "Dependency Graph",
                dependencies
        );

        this.scripts.add(chart.getUrl());

        return chart;
    }

    private BarChart createCloneChart(Project project, Clones<KeywordDefinition> clones) throws IOException {
        BarChart chart = CloneChart.create(getId(), project.getUserKeywords(), clones);
        this.scripts.add(chart.getUrl());

        return chart;
    }

    private void getDependencies(Project project, Set<Dependency> dependencies, Set<Project> memory){
        if(memory.contains(project)){
            return;
        }

        memory.add(project);

        for(Project dependency: project.getDependencies()){
            dependencies.add(new Dependency(
                    StringUtils.toBeautifulName(dependency.getName()),
                    StringUtils.toBeautifulName(project.getName()),
                    Dependency.Type.UserProject
            ));

            getDependencies(dependency, dependencies, memory);
        }
    }

    public Link getLink() {
        return link;
    }

    public int getNumberKeywords() {
        return numberKeywords;
    }

    public int getNumberTestCases() {
        return numberTestCases;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public BarChart getSizeChart() {
        return sizeChart;
    }

    public BarChart getConnectivityChart() {
        return connectivityChart;
    }

    public BarChart getDepthChart() {
        return depthChart;
    }

    public BarChart getSequenceChart() {
        return sequenceChart;
    }

    public DependencyGraph getDependencyGraph() {
        return dependencyGraph;
    }

    public BarChart getCloneChart() {
        return cloneChart;
    }

    public List<String> getScripts() {
        return scripts;
    }

    private List<String> getKeys(Map<Integer, Integer> map){
        List<String> keys = new ArrayList<>(map.size());
        for (Map.Entry<Integer,Integer> entry : map.entrySet()){
            keys.add(String.valueOf(entry.getKey()));
        }
        return keys;
    }

    private List<Integer> getValues(Map<Integer, Integer> map){
        List<Integer> keys = new ArrayList<>(map.size());
        for (Map.Entry<Integer,Integer> entry : map.entrySet()){
            keys.add(entry.getValue());
        }
        return keys;
    }
}
