package org.ukwikora.inspector.dashboard;

import freemarker.template.*;
import org.joda.time.DateTime;
import org.ukwikora.analytics.CloneDetection;
import org.ukwikora.analytics.Clones;
import org.ukwikora.inspector.dashboard.model.*;
import org.ukwikora.model.Project;
import org.ukwikora.model.UserKeyword;
import org.ukwikora.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class StatisticsViewerGenerator {
    private List<Project> projects;
    private File destination;

    public StatisticsViewerGenerator(List<Project> projects, File destination){
        this.projects = projects;
        this.destination = destination;
    }

    public void generate() throws Exception {
        Clones<UserKeyword> clones = computeClones();

        SideBar sideBar = new SideBar(projects);
        Map<String, Object> input = new HashMap<>();
        input.put("sidebar", sideBar);
        input.put("generated_date", DateTime.now().toLocalDate().toString());

        copyResources();
        generateSummaryPage(new HashMap<>(input), clones);
        generateDependenciesPage(new HashMap<>(input));
        generateDeadCodePage(new HashMap<>(input));
        generateClonePage(new HashMap<>(input), clones);
        generateViolationsPage(new HashMap<>(input));

        for(Project project: projects){
            generateSingleProjectPage(project, new HashMap<>(input));
        }
    }

    void copyResources() throws Exception {
        File source = FileUtils.getResourceFile("static");
        org.apache.commons.io.FileUtils.copyDirectory(source, destination);
    }

    private Clones<UserKeyword> computeClones(){
        return CloneDetection.findClones(new HashSet<>(projects), UserKeyword.class);
    }

    private void generateSummaryPage(Map<String, Object> input, Clones<UserKeyword> clones) throws Exception {
        SummaryPage summaryPage = new SummaryPage("index", "Summary", projects, clones);

        input.put("summaryPage", summaryPage);
        processTemplate("summary.ftl", input, new File(destination, "index.html"));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", summaryPage.getLinesChart()),
                new File(destination, summaryPage.getLinesChart().getUrl()));

        processTemplate("lib/bar-chart.ftl",  Collections.singletonMap("chart", summaryPage.getUserKeywordsChart()),
                new File(destination, summaryPage.getUserKeywordsChart().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", summaryPage.getTestCasesChart()),
                new File(destination, summaryPage.getTestCasesChart().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", summaryPage.getCloneChart()),
                new File(destination, summaryPage.getCloneChart().getUrl()));
    }

    private void generateDependenciesPage(Map<String, Object> input) throws Exception {
        DependencyPage dependencies = new DependencyPage("projects-dependency-graph",
                "Dependency Graph", projects);

        input.put("dependencies", dependencies);

        processTemplate("dependencies.ftl", input, new File(destination, "dependencies.html"));

        processTemplate("lib/dependency-graph.ftl", Collections.singletonMap("chart", dependencies.getGraph()),
                new File(destination, dependencies.getGraph().getUrl()));
    }

    private void generateDeadCodePage(Map<String, Object> input) throws Exception {
        DeadCodePage deadCodePage = new DeadCodePage("dead-code", "Dead Code", projects);

        input.put("deadCodePage", deadCodePage);

        processTemplate("dead-code.ftl", input, new File(destination, "dead-code.html"));
    }

    private void generateClonePage(Map<String, Object> input, Clones<UserKeyword> clones) throws Exception {
        ClonePage clonePage = new ClonePage("clones", "Duplicated Code", clones);

        input.put("data", clonePage.getTable());
        processTemplate("lib/table-js.ftl", input, new File(destination, clonePage.getTable().getUrl()));

        input.put("clones", clonePage);
        processTemplate("clones.ftl", input, new File(destination, "clones.html"));
    }

    private void generateViolationsPage(Map<String, Object> input) throws  Exception{
        ViolationsPage violationPage = new ViolationsPage("violations", "Violations", projects);

        input.put("violations", violationPage);

        processTemplate("violations.ftl", input, new File(destination, "violations.html"));
    }

    private void generateSingleProjectPage(Project project, Map<String, Object> input) throws Exception {
        SingleProjectPage singleProjectPage = new SingleProjectPage(project);

        input.put("project", singleProjectPage);

        processTemplate("project.ftl", input, new File(destination, singleProjectPage.getLink().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", singleProjectPage.getConnectivityChart()),
                new File(destination, singleProjectPage.getConnectivityChart().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", singleProjectPage.getSizeChart()),
                new File(destination, singleProjectPage.getSizeChart().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", singleProjectPage.getDepthChart()),
                new File(destination, singleProjectPage.getDepthChart().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", singleProjectPage.getSequenceChart()),
                new File(destination, singleProjectPage.getSequenceChart().getUrl()));

        processTemplate("lib/dependency-graph.ftl", Collections.singletonMap("chart", singleProjectPage.getDependencyGraph()),
                new File(destination, singleProjectPage.getDependencyGraph().getUrl()));
    }

    private Template getTemplate(String name) throws Exception {
        Configuration cfg = new Configuration();

        File templateDirectory = FileUtils.getResourceFile("static/ftl");
        cfg.setDirectoryForTemplateLoading(templateDirectory);

        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        return cfg.getTemplate(name);
    }

    private void processTemplate(String name, Map<String, Object> input, File output) throws Exception {
        try(Writer fileWriter = new FileWriter(output)) {
            Template template = getTemplate(name);
            template.process(input, fileWriter);
        }
    }
}
