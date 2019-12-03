package org.ukwikora.inspector.dashboard.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.analytics.Violation;
import org.ukwikora.analytics.ViolationDetection;
import org.ukwikora.inspector.dashboard.InvalidNumberColumnException;
import org.ukwikora.model.Project;

import java.util.Set;

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

        for(Violation violation: ViolationDetection.detect(projects)){
            try{
                this.table.addRow(new String[]{
                        violation.getLevel().name(),
                        violation.getNode().getName(),
                        violation.getNode().getFileName(),
                        violation.getNode().getLineRange().toString(),
                        violation.getNode().getFile().getProject().getName(),
                        violation.getCause().toString()
                });
            }catch (Exception e){
                logger.error(String.format("Failed to generate row: %s", e.getMessage()));
            }
        }
    }

    public Table getTable() {
        return table;
    }
}
