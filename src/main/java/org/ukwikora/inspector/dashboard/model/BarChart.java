package org.ukwikora.inspector.dashboard.model;

import org.ukwikora.utils.JsonUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class BarChart extends Chart{
    private final String jsonDatasets;
    private final String jsonLabels;

    private String yLabel;
    private String xLabel;

    private boolean displayLegend;

    public BarChart(String id, String name, ChartDataset dataSet, List<String> labels) throws IOException {
        super(id, name);

        this.jsonDatasets = JsonUtils.convertToJsonArray(Collections.singletonList(dataSet));
        this.jsonLabels = JsonUtils.convertToJsonArray(labels);

        this.yLabel = "";
        this.xLabel = "";

        this.displayLegend = false;
    }

    public BarChart(String id, String name, List<ChartDataset> dataSets, List<String> labels) throws IOException {
        super(id, name);

        this.jsonDatasets = JsonUtils.convertToJsonArray(dataSets);
        this.jsonLabels = JsonUtils.convertToJsonArray(labels);

        this.yLabel = "";
        this.xLabel = "";

        this.displayLegend = dataSets.size() > 1;
    }

    public boolean isDisplayLegend() {
        return displayLegend;
    }

    public void setDisplayLegend(boolean displayLegend) {
        this.displayLegend = displayLegend;
    }

    public String getJsonDatasets() {
        return jsonDatasets;
    }

    public String getJsonLabels() {
        return jsonLabels;
    }

    public String getYLabel() {
        return yLabel;
    }

    public void setYLabel(String yLabel) {
        this.yLabel = yLabel;
    }

    public String getXLabel() {
        return xLabel;
    }

    public void setXLabel(String xLabel) {
        this.xLabel = xLabel;
    }
}
