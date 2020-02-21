package tech.ikora.inspector.dashboard.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.ikora.analytics.Violation;
import tech.ikora.analytics.ViolationDetection;
import tech.ikora.inspector.dashboard.InvalidNumberColumnException;
import tech.ikora.model.Project;

import java.util.Set;

import static tech.ikora.analytics.Violation.Cause.MULTIPLE_DEFINITIONS;
import static tech.ikora.analytics.Violation.Cause.TRANSITIVE_DEPENDENCY;

public class ViolationsPage extends Page{
    private final Table table;
    private final Logger logger = LogManager.getLogger(ViolationsPage.class);

    public ViolationsPage(String id, String name, Set<Project> projects) throws InvalidNumberColumnException {
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
                            violation.getNode().getName().toString(),
                            violation.getNode().getFileName(),
                            violation.getNode().getRange().toString(),
                            violation.getNode().getProject().getName(),
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
