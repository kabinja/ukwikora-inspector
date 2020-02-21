package tech.ikora.inspector.dashboard.model;

import tech.ikora.analytics.CloneCluster;
import tech.ikora.analytics.Clones;
import tech.ikora.model.UserKeyword;

public class ClonePage extends Page {
    private final Table table;

    public ClonePage(String id, String name, Clones<UserKeyword> clones) throws Exception {
        super(id, name);

        this.table = new Table(
                id,
                "Duplicated Code",
                new String[]{"Group", "Size", "Type", "Dispersion", "Name", "File", "Lines", "Project"}
        );

        int i = 0;
        for(CloneCluster<UserKeyword> cluster: clones.getClusters()){
            int size = cluster.size();
            for(UserKeyword clone: cluster.getClones()){
                this.table.addRow(new String[]{
                        String.valueOf(i),
                        String.valueOf(size),
                        cluster.getType().name(),
                        cluster.isCrossProject() ? "CROSS" : "SINGLE",
                        clone.getName().toString(),
                        clone.getFileName(),
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
