package cs.cmu.cs214.framework.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javafx.util.Pair;

import cs.cmu.cs214.APIResponse;
import cs.cmu.cs214.ProcessedData;
import cs.cmu.cs214.Song;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class FrameworkImpl implements Framework {
    public static final String NOAPPNAME = "A music playlist analysis framework";
    public static final String DEFAULTFOOTER = "No ongoing operation";
    private String footer;

    private VisualPlugin currVisualPlugin;
    private DataPlugin currDataPlugin;

    private ArrayList<DataPlugin> registeredDataPlugins;
    private ArrayList<VisualPlugin> registeredVisualPlugins;

    private APIResponse parsedData;
    private ProcessedData processedData;
    private boolean processDone;

    private String currDisplayJson = "";

    /**
     * Creates a new {@link FrameworkImpl} instance.
     */
    public FrameworkImpl() {
        reset();
        registeredDataPlugins = new ArrayList<>();
        registeredVisualPlugins = new ArrayList<>();
    }

    public void reset() {
        footer = DEFAULTFOOTER;
        currVisualPlugin = null;
        currDataPlugin = null;
        parsedData = null;
        processedData = null;
        processDone = false;
        currDisplayJson = "";
    }

    public void registerDataPlugin(DataPlugin plugin) {
        plugin.onRegister(this);
        this.registeredDataPlugins.add(plugin);
    }

    public void registerVisualPlugin(VisualPlugin plugin) {
        plugin.onRegister(this);
        this.registeredVisualPlugins.add(plugin);
    }

    // Asks the correct plugin to process the link sent by a user and
    // formats the response from the plugin for further processing.
    // This method is private because it is only one step in the analysis
    // and it does not need to be called on its own.
    private void getPlaylistData(DataPlugin plugin, String link) {
        if (currDataPlugin != plugin) {
            if (currDataPlugin != null) {
                currDataPlugin.onFinished();
            }
            currDataPlugin = plugin;
        }
        this.parsedData = new APIResponse(plugin.downloadRawPlayList(link));
    }

    // Processes the artists of songs and returns a mapping from an artist to
    // the number of times that artist appears in the playlist. This method is private
    // because the framework is responsible for this analysis and nobody outside
    // of the framework should be able to access this analysis on its own.
    private Map<String, Integer> processArtists() {
        ArrayList<Song> songs = parsedData.getSongs();
        Map <String, Integer> artistsParsed = new HashMap<String, Integer>();
        for (Song s : songs) {
            String artist = s.getArtist();
            if(artistsParsed.containsKey(artist)) {
                artistsParsed.put(artist, artistsParsed.get(artist) + 1);
            } else {
                artistsParsed.put(artist, 1);
            }
        }
        return artistsParsed;
    }

    public static final Integer SECPERMIN = 60;

    private Pair<Integer, Integer> computeBounds(Long seconds) {
        return new Pair(seconds / SECPERMIN, (seconds / SECPERMIN) + 1);
    }

    public static final Integer SECONDS = 1000;

    // Processes the duration of songs and returns a mapping from a duration range
    // to a list of songs which fall within said range. This method is private
    // because the framework is responsible for this analysis and nobody outside
    // of the framework should be able to access this analysis on its own.
    private Map<Pair<Integer, Integer>, ArrayList<Long>> processDuration() {
        ArrayList<Song> songs = parsedData.getSongs();
        Map<Pair<Integer, Integer>, ArrayList<Long>> durationsParsed = new HashMap<Pair<Integer, Integer>, ArrayList<Long>>();
        for (Song s : songs) {
            Long duration = s.getDurationMS();
            Long inSeconds = duration / SECONDS;
            Pair<Integer, Integer> key = computeBounds(inSeconds);
            ArrayList<Long> list;
            if(durationsParsed.containsKey(key)) {
                list = (durationsParsed.get(key));
            } else {
                list = new ArrayList<>();
            }
            list.add(inSeconds);
            durationsParsed.put(key, list);
        }
        return durationsParsed;
    }

    // Processes the sentiment for songs and returns a mapping from specific song to its
    // sentiment. This method is private because the framework is responsible for
    // this analysis and nobody outside of the framework should be able to access
    // this analysis on its own.
    private Map<Song, String> processSongSentiments() {
        // 1st: set up pipeline for standford coreNLP
        Properties props;
	    StanfordCoreNLP pipeline;
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
        Map<Song, String> songSentiments = new HashMap<>();

        // 2nd: iterate through all songs provided by data plugin and classify them
        for (Song song : parsedData.getSongs()) {
            Annotation annotation = pipeline.process(song.getTitle());
            CoreMap sentence = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
            String sentimentType = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            songSentiments.put(song, sentimentType);

        } 
        return songSentiments;
    }

    // Processes the sentiment categories and and returns a mapping from a sentiment to the
    // list of songs which fall under that sentiment. This method is private because the
    // framework is responsible for this analysis and nobody outside of the framework should
    // be able to access this analysis on its own.
    private Map<String, ArrayList<Song>> processSentimentCategories(Map<Song, String> songSentiments) {
        // 1st: set up pipeline for standford coreNLP
        Map<String, ArrayList<Song>> sentimentParsed = new HashMap<>();
        // 2nd: iterate through all songs provided by data plugin and classify them
        for (Song song : songSentiments.keySet()) {
            String sentimentType = songSentiments.get(song);
            ArrayList<Song> sentimentSongList = sentimentParsed.getOrDefault(sentimentType, new ArrayList<>());
            sentimentSongList.add(song);
            sentimentParsed.put(sentimentType, sentimentSongList);
        }
        return sentimentParsed;
    }

    // Collects the processed data as needed and transforms it into one fully processed object.
    // This method is private because it is one step in the analysis and it does not
    // need to be called on its own.
    private void processData() {
        ArrayList<Song> original = parsedData.getSongs();
        Map<String, Integer> artists = processArtists();
        Map<Pair<Integer, Integer>, ArrayList<Long>> durations = processDuration();
        Map<Song, String> songSentiments = processSongSentiments();
        Map<String, ArrayList<Song>> sentiments = processSentimentCategories(songSentiments);
        this.processedData = new ProcessedData(original, artists, durations, sentiments, songSentiments);
    }

    /**
     * The main method which is responsible for analyzing a user's playlist
     * 
     * @param plugin The plugin which needs to process the link
     * @param link The link to the playlist to be processed
     */
    public void analyze(DataPlugin plugin, String link) {
        getPlaylistData(plugin, link);
        processData();
    }

    public void display(VisualPlugin plugin) {
        if (currVisualPlugin != plugin) {
            if (currVisualPlugin != null) {
                currVisualPlugin.onFinished();
            }
            currVisualPlugin = plugin;
        }
        currVisualPlugin.onDisplay();
    }

    /**
     * Get the complete processed data
     */
    @Override
    public ProcessedData getProcessedData() {
        return this.processedData;
    }

    public String getChartJSON() {
        return this.currDisplayJson;
    }

    @Override
    public void setChartJSON(String chartJson) {
        this.currDisplayJson = chartJson;
    }

    public String getFooter() {
        return this.footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getName() {
        return NOAPPNAME;
    }

    public VisualPlugin getCurrVisualPlugin() {
        return this.currVisualPlugin;
    }

    public DataPlugin getCurrDataPlugin() {
        return this.currDataPlugin;
    }

    /**
     * @return Array of the registered plugins
     */
    public ArrayList<DataPlugin> getRegisteredDataPlugins() {
        return this.registeredDataPlugins;
    }

    public ArrayList<String> getRegisteredDataPluginName(){
        return (ArrayList<String>) this.registeredDataPlugins.stream().map(DataPlugin::getName).collect(Collectors.toList());
    }

    public ArrayList<String> getRegisteredVisualPluginName(){
        return (ArrayList<String>) this.registeredVisualPlugins.stream().map(VisualPlugin::getName).collect(Collectors.toList());
    }

    public boolean getProcessDone() {
        return this.processDone;
    }
}
