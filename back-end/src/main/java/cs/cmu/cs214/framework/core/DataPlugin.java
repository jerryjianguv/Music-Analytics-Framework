package cs.cmu.cs214.framework.core;

import org.json.simple.JSONObject;

public interface DataPlugin {
    /**
     * Uses the link provided by a user to extract the relevant information from a public playlist
     * 
     * The returned JSONObject contains the following information:
     *    "songs" -- A JSONArray
     * For each entry in the "songs" array:
     *    "duration" -- The duration of the song in milliseconds
     *    "artist" -- The artist of the song
     *    "title" - The title/name of the song
     * 
     * Note: for the existing plugins, the playlist MUST be public, otherwise this query will not work
     * 
     * @param link The link to the public playlist which will be analyzed by the framework
     * @return A JSONObject with the fields described above
     */
    JSONObject downloadRawPlayList(String link);

    void onRegister(Framework framework);
    void onFinished();

    String getName();
}
