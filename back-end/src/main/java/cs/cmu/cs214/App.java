package cs.cmu.cs214;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.ServiceLoader;

import cs.cmu.cs214.framework.core.DataPlugin;
import cs.cmu.cs214.framework.core.FrameworkImpl;
import cs.cmu.cs214.framework.core.VisualPlugin;
import cs.cmu.cs214.framework.gui.FrameworkState;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {
    private static final int PORT = 8080;
    private FrameworkImpl framework;
    private ArrayList<DataPlugin> dataPlugins;
    private ArrayList<VisualPlugin> visualPlugins;

    /**
     * Starts server
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    /**
     * Creates a new {@link App} instance.
     * 
     * @throws IOException
     */
    public App() throws IOException {
        super(PORT);
        this.framework = new FrameworkImpl();
        this.dataPlugins = loadDataPlugins();
        this.visualPlugins = loadVisualPlugins();

        // register data and visual plugins
        for (DataPlugin dataPlugin : this.dataPlugins) {
            this.framework.registerDataPlugin(dataPlugin);
        }

        for (VisualPlugin visualPlugin : this.visualPlugins) {
            this.framework.registerVisualPlugin(visualPlugin);
        }

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("Running!");
    }

    /**
     * Checks the URI of the incoming request and calls the corresponding
     * method in the FrameworkImpl instance. After processing the request,
     * extracts the state of the "FrameworkImpl" instance and returns it as a response.
     */
    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();

        if (uri.equals("/getData")) {
            String link = params.get("link");
            this.framework.analyze(this.dataPlugins.get(Integer.parseInt(params.get("i"))), link);
        } else if (uri.equals("/display")) {
            this.framework.display(this.visualPlugins.get(Integer.parseInt(params.get("i"))));
        } else if (uri.equals("/start")) {
            this.framework.reset();
        }
        FrameworkState songs = FrameworkState.forApp(this.framework);
        System.out.println("songs are " + songs + "\n");
        return newFixedLengthResponse(songs.toString());
    }

    /**
     * Loads all the available visual plugins, then prints
     * the names of the loaded plugins to the console.
     * 
     * @return The available visual plugins in a list
     */
    private static ArrayList<VisualPlugin> loadVisualPlugins() {
        ServiceLoader<VisualPlugin> visualPlugins = ServiceLoader.load(VisualPlugin.class);
        ArrayList<VisualPlugin> result = new ArrayList<>();
        for (VisualPlugin plugin : visualPlugins) {
            System.out.println("Loaded visual plugin " + plugin.getName());
            result.add(plugin);
        }
        result.sort(Comparator.comparing(VisualPlugin::getName).reversed());
        return result;
    }

    /**
     * Loads all the available data plugins, then prints
     * the names of the loaded plugins to the console.
     * 
     * @return The available data plugins in a list
     */
    private static ArrayList<DataPlugin> loadDataPlugins() {
        ServiceLoader<DataPlugin> dataPlugins = ServiceLoader.load(DataPlugin.class);
        ArrayList<DataPlugin> result = new ArrayList<>();
        for (DataPlugin plugin : dataPlugins) {
            System.out.println("Loaded data plugin " + plugin.getName());
            result.add(plugin);
        }
        result.sort(Comparator.comparing(DataPlugin::getName).reversed());
        return result;
    } 
}

