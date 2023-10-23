package cs.cmu.cs214.plugin.visual;

import java.util.Map;

import org.icepear.echarts.Pie;
import org.icepear.echarts.charts.pie.PieDataItem;
import org.icepear.echarts.render.Engine;

import cs.cmu.cs214.ProcessedData;
import cs.cmu.cs214.framework.core.Framework;
import cs.cmu.cs214.framework.core.VisualPlugin;

/**
 * The PieChartPlugin class visualizes the frequency of artists in the playlist
 */
public class PieChartPlugin implements VisualPlugin {
    public static final String DISPLAY_TYPE = "Pie";
    public static final String CHART_TITLE = "Pie Chart for Artist Frequency";
    private Framework framework;
    
    private ProcessedData inputData; 
    private Pie pieChart;
    @Override
    public String getDisplayType() {
        return DISPLAY_TYPE;
    }

    @Override
    public String getName() {
        return "Pie Chart For Artists";
    }

    private PieDataItem[] getDataItem() {
        Map<String, Integer> artistFreq = this.inputData.getArtistFrequencies();
        PieDataItem[] dataItem = new PieDataItem[artistFreq.size()];
        int i = 0;
        for (String artist : artistFreq.keySet()) {
            dataItem[i] = new PieDataItem()
                .setValue(artistFreq.get(artist))
                .setName(artist);
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
        this.pieChart = new Pie()
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
        String chartJson = engine.renderJsonOption(this.pieChart);
        this.framework.setChartJSON(chartJson);
    }

    @Override
    public void onGetInput() {
        this.inputData = this.framework.getProcessedData();
    }

    @Override
    public void onFinished() {
    }
}
