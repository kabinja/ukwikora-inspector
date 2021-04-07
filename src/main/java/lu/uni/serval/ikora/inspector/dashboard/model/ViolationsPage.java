package lu.uni.serval.ikora.inspector.dashboard.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lu.uni.serval.ikora.core.analytics.violations.Violation;
import lu.uni.serval.ikora.core.analytics.violations.ViolationDetection;
import lu.uni.serval.ikora.core.model.Projects;

import static lu.uni.serval.ikora.core.analytics.violations.Violation.Cause.MULTIPLE_DEFINITIONS;
import static lu.uni.serval.ikora.core.analytics.violations.Violation.Cause.TRANSITIVE_DEPENDENCY;

public class ViolationsPage extends Page{
    private final Table table;
    private final Logger logger = LogManager.getLogger(ViolationsPage.class);

    public ViolationsPage(String id, String name, Projects projects) {
        super(id, name);

        this.table = new Table(
          id,
          "Violations",
          new String[]{"Type", "Name", "File", "Lines", "Project", "Message"}
        );

        Violation.Cause[] causes = new Violation.Cause[]{
                MULTIPLE_DEFINITIONS,
                TRANSITIVE_DEPENDENCY
        };

        for(Violation.Cause cause: causes){
            for(Violation violation: ViolationDetection.detect(projects, cause)){
                try{
                    this.table.addRow(new String[]{
                            violation.getLevel().name(),
                            violation.getSourceNode().getName(),
                            violation.getSourceNode().getLibraryName(),
                            violation.getSourceNode().getRange().toString(),
                            violation.getSourceNode().getProject().getName(),
                            violation.getCause().toString()
                    });
                }catch (Exception e){
                    logger.error(String.format("Failed to generate row: %s", e.getMessage()));
                }
            }
        }
    }

    public Table getTable() {
        return table;
    }
}
