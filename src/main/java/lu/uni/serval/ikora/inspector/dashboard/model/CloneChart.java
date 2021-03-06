package lu.uni.serval.ikora.inspector.dashboard.model;

import lu.uni.serval.ikora.core.analytics.clones.Clones;
import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.UserKeyword;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CloneChart {
    public static BarChart create(String prefixId, Set<UserKeyword> keywords, Clones<KeywordDefinition> clones) throws IOException {
        int linesOfCode = getLinesOfCode(keywords);

        List<Double> values = new ArrayList<>(4);
        values.add(getPercentageClones(Clones.Type.TYPE_1, clones, keywords, linesOfCode));
        values.add(getPercentageClones(Clones.Type.TYPE_2, clones, keywords, linesOfCode));
        values.add(getPercentageClones(Clones.Type.TYPE_3, clones, keywords, linesOfCode));
        values.add(getPercentageClones(Clones.Type.TYPE_4, clones, keywords, linesOfCode));

        List<String> labels = new ArrayList<>();
        labels.add("Type I");
        labels.add("Type II");
        labels.add("Type III");
        labels.add("Type IV");

        ChartDataset dataset = new ChartDataset("Clones", values, ChartDataset.Color.BLUE);

        BarChart cloneChart = new BarChart(
                prefixId.isEmpty() ? "summary-clones-chart": String.format("%s-summary-clones-chart", prefixId),
                "Percentage of lines duplicated",
                dataset,
                labels);

        cloneChart.setYLabel("Percent");

        return cloneChart;
    }

    private static int getLinesOfCode(Set<UserKeyword> keywords){
        return keywords.stream()
                .mapToInt(UserKeyword::getLoc)
                .sum();
    }

    private static double getPercentageClones(Clones.Type type, Clones<KeywordDefinition> clones, Set<UserKeyword> keywords, int linesOfCode){
        double numberOfCloneLines = 0;

        for(UserKeyword keyword: keywords){
            if(clones.getCloneType(keyword) == type){
                numberOfCloneLines += keyword.getLoc();
            }
        }

        return (numberOfCloneLines / linesOfCode) * 100;
    }
}
