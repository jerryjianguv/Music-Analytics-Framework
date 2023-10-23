package cs.cmu.cs214.framework.gui;

import java.util.ArrayList;
import java.util.Arrays;

import cs.cmu.cs214.framework.core.FrameworkImpl;

public class FrameworkState {
    private final String name;
    private final String footer;
    private final Plugin[] dataPlugins;
    private final Plugin[] visuaPlugins;
    private final String graphJson;
    private final boolean processDone;

    public FrameworkState(String name, String footer, Plugin[] dataPlugins, Plugin[] visuaPlugins, String graphJson, boolean processDone) {
        this.name = name;
        this.footer = footer;
        this.dataPlugins = dataPlugins;
        this.visuaPlugins = visuaPlugins;
        this.graphJson = graphJson;
        this.processDone = processDone;
    }

    /**
     * Returns new FrameworkState to be used by the app
     * 
     * @param framework The current instance of the framework
     * @return A FrameworkState
     */
    public static FrameworkState forApp(FrameworkImpl framework) {
        String name = framework.getName();
        String footer = framework.getFooter();
        Plugin[] dataPlugins = getPlugins(framework, "data");
        Plugin[] visualPlugins = getPlugins(framework, "visual");
        String graphJson = framework.getChartJSON();
        boolean processDone = framework.getProcessDone();
        return new FrameworkState(name, footer, dataPlugins, visualPlugins, graphJson, processDone);
    }

    private static Plugin[] getPlugins(FrameworkImpl framework, String pluginType) {
        ArrayList<String> gamePlugins = pluginType == "data" ? framework.getRegisteredDataPluginName() : framework.getRegisteredVisualPluginName();
        Plugin[] plugins = new Plugin[gamePlugins.size()];
        for (int i=0; i<gamePlugins.size(); i++){
            plugins[i] = new Plugin(gamePlugins.get(i));
        }
        return plugins;
    }

    public String getName() {
        return this.name;
    }

    public String getFooter() {
        return this.footer;
    }

    public Plugin[] getDataPlugins() {
        return this.dataPlugins;
    }

    public Plugin[] getVisualPlugins() {
        return this.visuaPlugins;
    }

    public String getGraphJson() {
        return graphJson;
    }
    
    @Override
    public String toString() {
        if (this.graphJson == "") {
            return """
            {
                "name": "%s",
                "footer": "%s", 
                "dataPlugins": %s, 
                "visualPlugins": %s, 
                "graphJson": "%s",
                "processDone": %b
            }
            """.formatted(this.name, this.footer, Arrays.toString(this.dataPlugins), Arrays.toString(this.visuaPlugins), this.graphJson, this.processDone); 
        }
        return """
            {
                "name": "%s",
                "footer": "%s", 
                "dataPlugins": %s, 
                "visualPlugins": %s, 
                "graphJson": "%s",
                "processDone": %b
            }
            """.formatted(this.name, this.footer, Arrays.toString(this.dataPlugins), Arrays.toString(this.visuaPlugins),
                            this.graphJson.replaceAll("\"", "\\\\\""), this.processDone);
    } 
}