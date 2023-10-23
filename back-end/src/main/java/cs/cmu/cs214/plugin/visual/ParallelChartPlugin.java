package cs.cmu.cs214.plugin.visual;

import java.util.List;
import java.util.Map;

import org.icepear.echarts.Parallel;
import org.icepear.echarts.charts.parallel.ParallelSeries;
import org.icepear.echarts.components.series.LineStyle;
import org.icepear.echarts.render.Engine;

import cs.cmu.cs214.ProcessedData;
import cs.cmu.cs214.Song;
import cs.cmu.cs214.framework.core.Framework;
import cs.cmu.cs214.framework.core.VisualPlugin;

/**
 * The ParallelChartPlugin class visualizes individual songs across both duration and sentiment
 */
public class ParallelChartPlugin implements VisualPlugin {
    public static final String DISPLAYTYPE = "Parallel Coord";
    public static final String CHARTTITLE = "Parallel Coordinate Chart for Duration and Sentiment Visualization";
    private Framework framework;
    private ProcessedData inputData;
    private Parallel parallelChart;

    @Override
    public String getDisplayType() {
        return DISPLAYTYPE;
    }

    @Override
    public String getName() {
        return "Parallel Chart for Sentiment And Duration";
    }

    public static final Integer WIDTH = 4;

    private ParallelSeries getParallelSeries() {
        List<Song> songList = this.inputData.getOriginalPlaylist();
        Map<Song, String> songSentiments = this.inputData.getSongSentiment();
        Object[][] objs = new Object[songList.size()][2];
        int i = 0;
        for (Song song : songList) {
            objs[i][1] = song.getDurationMS();
            objs[i][0] = songSentiments.get(song);
            i++;
        }
        return new ParallelSeries().setLineStyle(new LineStyle().setWidth(WIDTH)).setData(objs);
    }

    @Override
    public void onRegister(Framework framework) {
        this.framework = framework;
    }
    
    @Override
    public void draw(ProcessedData data) {
        this.parallelChart = new Parallel()
                        .addParallelAxis("Sentiment", 0, new String[]{"Positive", "Neutral", "Negative"})
                        .addParallelAxis("Duration", 1)
                        .addSeries(getParallelSeries());
    }

    @Override
    public void onDisplay() {
        onGetInput();
        if (this.inputData == null) {
            return;
        }
        draw(this.inputData);
        Engine engine = new Engine();
        String chartJson = engine.renderJsonOption(this.parallelChart);
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
