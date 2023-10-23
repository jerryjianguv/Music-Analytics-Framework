package cs.cmu.cs214.framework.core;

import cs.cmu.cs214.ProcessedData;

public interface Framework {
    /**
     * Retrive the data gathered from processing the playlist
     * in the framework
     * 
     * @return An organized representation of the processed data
     */
    ProcessedData getProcessedData();

    String getChartJSON();
    void setChartJSON(String chartJson);

    String getFooter();
    void setFooter(String footer);
}
