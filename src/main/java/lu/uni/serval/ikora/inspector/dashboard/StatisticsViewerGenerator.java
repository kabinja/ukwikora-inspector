package lu.uni.serval.ikora.inspector.dashboard;

import freemarker.template.*;
import lu.uni.serval.ikora.inspector.dashboard.model.*;
import lu.uni.serval.ikora.core.analytics.clones.Clones;
import lu.uni.serval.ikora.core.analytics.clones.KeywordCloneDetection;
import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.Projects;
import lu.uni.serval.ikora.core.utils.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatisticsViewerGenerator {
    private final Projects projects;
    private final File destination;

    public StatisticsViewerGenerator(Projects projects, File destination){
        this.projects = projects;
        this.destination = destination;
    }

    public void generate() throws Exception {
        Clones<KeywordDefinition> clones = computeClones();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        SideBar sideBar = new SideBar(projects);
        Map<String, Object> input = new HashMap<>();
        input.put("sidebar", sideBar);
        input.put("generated_date", LocalDateTime.now().format(formatter));

        FileUtils.copyResources(getClass(),"static", destination);
        generateDocumentationPage(new HashMap<>(input));
        generateSummaryPage(new HashMap<>(input), clones);
        generateDependenciesPage(new HashMap<>(input));
        generateDeadCodePage(new HashMap<>(input));
        generateClonePage(new HashMap<>(input), clones);
        generateViolationsPage(new HashMap<>(input));
        generateDictionaryPage(new HashMap<>(input));

        for(Project project: projects){
            generateSingleProjectPage(project, clones, new HashMap<>(input));
        }
    }

    private Clones<KeywordDefinition> computeClones(){
        return KeywordCloneDetection.findClones(projects);
    }

    private void generateDocumentationPage(Map<String, Object> input) throws Exception {
        processTemplate("documentation.ftl", input, new File(destination, "documentation.html"));
    }

    private void generateSummaryPage(Map<String, Object> input, Clones<KeywordDefinition> clones) throws Exception {
        SummaryPage summaryPage = new SummaryPage("index", "Summary", projects, clones);

        input.put("summaryPage", summaryPage);
        processTemplate("summary.ftl", input, new File(destination, "index.html"));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", summaryPage.getDeadCodeChart()),
                new File(destination, summaryPage.getDeadCodeChart().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", summaryPage.getDuplicatedChart()),
                new File(destination, summaryPage.getDuplicatedChart().getUrl()));

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

        input.put("data", deadCodePage.getTable());
        processTemplate("lib/table-js.ftl", input, new File(destination, deadCodePage.getTable().getUrl()));

        input.put("deadCodePage", deadCodePage);
        processTemplate("dead-code.ftl", input, new File(destination, "dead-code.html"));
    }

    private void generateClonePage(Map<String, Object> input, Clones<KeywordDefinition> clones) throws Exception {
        ClonePage clonePage = new ClonePage("clones", "Duplicated Code", clones);

        input.put("data", clonePage.getTable());
        processTemplate("lib/table-js.ftl", input, new File(destination, clonePage.getTable().getUrl()));

        input.put("clones", clonePage);
        processTemplate("clones.ftl", input, new File(destination, "clones.html"));
    }

    private void generateViolationsPage(Map<String, Object> input) throws  Exception{
        ViolationsPage violationPage = new ViolationsPage("violations", "Violations", projects);

        input.put("data", violationPage.getTable());
        processTemplate("lib/table-js.ftl", input, new File(destination, violationPage.getTable().getUrl()));

        input.put("violations", violationPage);
        processTemplate("violations.ftl", input, new File(destination, "violations.html"));
    }

    private void generateDictionaryPage(HashMap<String, Object> input)  throws  Exception{
        DictionaryPage dictionaryPage = new DictionaryPage("dictionary", "Dictionary", projects);

        input.put("data", dictionaryPage.getTable());
        processTemplate("lib/table-js.ftl", input, new File(destination, dictionaryPage.getTable().getUrl()));

        input.put("dictionary", dictionaryPage);
        processTemplate("dictionary.ftl", input, new File(destination, "dictionary.html"));
    }


    private void generateSingleProjectPage(Project project, Clones<KeywordDefinition> clones, Map<String, Object> input) throws Exception {
        SingleProjectPage singleProjectPage = new SingleProjectPage(project, clones);

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

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", singleProjectPage.getCloneChart()),
                new File(destination, singleProjectPage.getCloneChart().getUrl()));
    }

    private Template getTemplate(String name) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
        cfg.setClassForTemplateLoading(this.getClass(), "/ftl");

        cfg.setIncompatibleImprovements(new Version(2, 3, 21));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        return cfg.getTemplate(name);
    }

    private void processTemplate(String name, Map<String, Object> input, File output) throws Exception {
        try(OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8)) {
            Template template = getTemplate(name);
            template.process(input, fileWriter);
        }
    }
}
