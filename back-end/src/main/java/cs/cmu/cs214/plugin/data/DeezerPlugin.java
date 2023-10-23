package cs.cmu.cs214.plugin.data;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import api.deezer.DeezerApi;
import api.deezer.exceptions.DeezerException;
import api.deezer.objects.Playlist;
import api.deezer.objects.Track;
import cs.cmu.cs214.framework.core.DataPlugin;
import cs.cmu.cs214.framework.core.Framework;

public class DeezerPlugin implements DataPlugin {
    private final String accessToken = "";
    private DeezerApi deezerApi;
    private Framework framework;

    /**
     * Creates a new {@link DeezerPlugin} instance.
     * 
     * This method is the one responsible for setting up the connection to the Deezer API
     */
    public DeezerPlugin() {
        this.deezerApi = new DeezerApi(accessToken);
    }

    // helper method to extract the playlist id from a link
    private String extractId(String link) {
        int idx = link.lastIndexOf('/');
        return link.substring(idx + 1);
    }

    // helper method to create JSONObject to go into the final JSONArray
    private JSONObject populateTrack(Track track) {
        JSONObject trackJson = new JSONObject();
            trackJson.put("title", track.getTitle());
            trackJson.put("artist", track.getArtist().getName());
            trackJson.put("duration", (long)track.getDuration());
        return trackJson;
    }

    /**
     * Uses the link provided by a user to extract the relevant information from a public Deezer playlist
     * 
     * The returned JSONObject contains the following information:
     *    "songs" -- A JSONArray
     * For each entry in the "songs" array:
     *    "duration" -- The duration of the song in milliseconds
     *    "artist" -- The artist of the song
     *    "title" - The title/name of the song
     * 
     * Note: the playlist MUST be public, otherwise this query will not work
     * 
     * @return a JSONObject with the parsed data from the playlist passed in by a user
     */
    @Override
    public JSONObject downloadRawPlayList(String link) {
        String playlistId = extractId(link);
        Playlist playlist;
        List<Track> trackList;
        try {
            playlist = this.deezerApi.playlist().getById(Long.parseLong(playlistId)).execute();
            trackList = playlist.getTracks().getData();
        } catch (NumberFormatException | DeezerException e) {
            // TODO Auto-generated catch block
            System.out.println("Cannot get the playlist.");
            System.out.println("Error: " + e.getMessage());
            return new JSONObject();
        }
        JSONArray tracks = new JSONArray();
        for (Track track : trackList) {
            tracks.add(populateTrack(track));
        }
        JSONObject result = new JSONObject();
        result.put("songs", tracks);
        return result;
    }

    @Override
    public String getName() {
        return "Deezer";
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
