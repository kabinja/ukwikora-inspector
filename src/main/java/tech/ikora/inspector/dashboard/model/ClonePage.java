package tech.ikora.inspector.dashboard.model;

import tech.ikora.analytics.clones.CloneCluster;
import tech.ikora.analytics.clones.Clones;
import tech.ikora.model.KeywordDefinition;

public class ClonePage extends Page {
    private final Table table;

    public ClonePage(String id, String name, Clones<KeywordDefinition> clones) throws Exception {
        super(id, name);

        this.table = new Table(
                id,
                "Duplicated Code",
                new String[]{"Group", "Size", "Type", "Dispersion", "Name", "File", "Lines", "Project"}
        );

        int i = 0;
        for(CloneCluster<KeywordDefinition> cluster: clones.getClusters()){
            int size = cluster.size();
            for(KeywordDefinition clone: cluster){
                this.table.addRow(new String[]{
                        String.valueOf(i),
                        String.valueOf(size),
                        cluster.getType().name(),
                        cluster.isCrossProject() ? "CROSS" : "SINGLE",
                        clone.getName(),
                        clone.getLibraryName(),
                        clone.getRange().toString(),
                        clone.getProject().getName()
                });

            }
            ++i;
        }
    }

    public Table getTable() {
        return table;
    }

    public BarChart getChart() {
        return null;
    }
}
