package cs.cmu.cs214.plugin.visual;

import java.util.ArrayList;
import java.util.Map;

import org.icepear.echarts.Bar;
import org.icepear.echarts.charts.bar.BarDataItem;

import org.icepear.echarts.render.Engine;

import cs.cmu.cs214.ProcessedData;
import cs.cmu.cs214.framework.core.Framework;
import cs.cmu.cs214.framework.core.VisualPlugin;
import javafx.util.Pair;

/**
 * The BarChartPlugin class visualizes the distribution of song duration in a playlist
 */
public class BarChartPlugin implements VisualPlugin {
    public static final String DISPLAY_TYPE = "Bar";
    public static final String CHART_TITLE = "Bar Chart for Song Duration Visualization";
    private Framework framework;

    private ProcessedData inputData;
    private Bar barChart;

    @Override
    public String getDisplayType() {
        return DISPLAY_TYPE;
    }

    @Override
    public String getName() {
        String name = "Bar Chart for Song Duration";
        return name;
    }

    private BarDataItem[] getDataItem() {
        Map<Pair<Integer, Integer>, ArrayList<Long>> durations = this.inputData.getSongDurationCateogries();
        int size = durations.size();
        BarDataItem[] dataItem = new BarDataItem[size];

        int i = 0;
        for (Pair<Integer, Integer> duration : durations.keySet()) {
            dataItem[i] = new BarDataItem()
                .setValue(durations.get(duration).size())
                .setName(duration.getKey());
            i++;
        }

        return dataItem;
    }

    @Override
    public void onRegister(Framework framework) {
        this.framework = framework;
    }

    @Override
    public void draw(ProcessedData data) {
        this.barChart = new Bar()
        .addXAxis()
        .addYAxis()
        .setTitle(CHART_TITLE)
        .addSeries(this.getDataItem());
    }

    @Override
    public void onDisplay() {
        onGetInput();
        if (this.inputData == null) {
            return;
        }
        draw(this.inputData);
        Engine engine = new Engine();
        String chartJson = engine.renderJsonOption(this.barChart);
        this.framework.setChartJSON(chartJson);
    }

    @Override
    public void onGetInput() {
        this.inputData = this.framework.getProcessedData();
    }

    @Override
    public void onFinished() {
        return;
    }
    
}
