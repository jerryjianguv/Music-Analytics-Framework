package cs.cmu.cs214;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class APIResponse {
    private ArrayList<Song> songs = new ArrayList<Song>();

    /**
     * Creates a new {@link APIResponse} instance.
     * 
     * @param rawData The JSONObject representation of the parsed playlist recieved from a data plugin
     */
    public APIResponse(JSONObject rawData) {
        JSONArray unparsedSongs = (JSONArray) rawData.get("songs");
        for (Object o : unparsedSongs) {
            addSong((JSONObject) o);
        }
    }
    
    private void addSong(JSONObject o) {
        String title = (String) o.get("title");
        String artist = (String) o.get("artist");
        Long duration = (Long) o.get("duration");
        songs.add(new Song(title, artist, duration));
    }

    public ArrayList<Song> getSongs() {
        return this.songs;
    }
}
