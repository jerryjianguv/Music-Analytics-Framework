package cs.cmu.cs214.plugin.data;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import au.com.bytecode.opencsv.CSVReader;
import cs.cmu.cs214.framework.core.DataPlugin;
import cs.cmu.cs214.framework.core.Framework;

public class LocalFilePlugin implements DataPlugin {
    private Framework framework;

    private JSONObject populateTrack(String[] s) {
        String title = s[0];
        String artist = s[1];
        Long duration = Long.parseLong(s[2]);
        JSONObject track = new JSONObject();
        track.put("title", title);
        track.put("artist", artist);
        track.put("duration", duration);
        return track;
    }

    private String[] parse(String[] line) {
        for (int i = 0; i < line.length; i++) {
            line[i] = line[i].trim();
        }
        return line;
    }

     /**
     * Uses the link provided by a user to extract the relevant information from a local CSV file
     * 
     * The returned JSONObject contains the following information:
     *    "songs" -- A JSONArray
     * For each entry in the "songs" array:
     *    "duration" -- The duration of the song in milliseconds
     *    "artist" -- The artist of the song
     *    "title" - The title/name of the song
     * 
     * Note: the CSV file MUST follow the format below:
     *   Title, Artist, Duration
     * 
     * The CSV file must NOT have a header.
     * 
     * @return a JSONObject with the parsed data from the playlist passed in by a user
     */
    @Override
    public JSONObject downloadRawPlayList(String file) {
        JSONArray tracks = new JSONArray();
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(file));
            String [] nextLine;
            // Read one line at a time
            while ((nextLine = reader.readNext()) != null) {
                // Use the tokens as required
                tracks.add(populateTrack(parse(nextLine)));
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return new JSONObject();
        }
        JSONObject result = new JSONObject();
        result.put("songs", tracks);
        return result;
    }

    @Override
    public String getName() {
        return "Local File";
    }

    @Override
    public void onRegister(Framework framework) {
        this.framework = framework;
    }

    @Override
    public void onFinished() {
        // nothing to do here
    }
    
}
