package cs.cmu.cs214.plugin.data;
import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.apache.hc.core5.http.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import cs.cmu.cs214.framework.core.DataPlugin;
import cs.cmu.cs214.framework.core.Framework;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;

public class SpotifyPlugin implements DataPlugin {
    public static final String CLIENTID = "5d067854c04c473881c2660cfc71e1ca";
    public static final String CLIENTSECRET = "02053e4cc75a44cfa5b8e82867ae2dd7";
    private SpotifyApi spotifyApi;
    private Framework framework;

    /**
     * Creates a new {@link SpotifyPlugin} instance.
     * 
     * This method is the one responsible for setting up the connection to the Spotify API
     */
    public SpotifyPlugin() {
        try {
            String accessToken = new ClientCredentialsRequest
                .Builder(CLIENTID, CLIENTSECRET)
                .grant_type("client_credentials")
                .build()
                .execute()
                .getAccessToken();

            spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .setClientId(CLIENTID)
                .setClientSecret(CLIENTSECRET)
                .build();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // helper method to extract the playlist id from a link
    private String extractId(String link) {
        int idx = link.lastIndexOf('/');
        return link.substring(idx + 1);
    }

    // helper method to create JSONObject to go into the final JSONArray
    private JSONObject populateTrack(JSONObject o) {
        JSONObject t = (JSONObject) o.get("track");
        JSONArray artists = (JSONArray) t.get("artists");
        Object firstArtist = artists.get(0);
        String artist = (String) ((JSONObject) firstArtist).get("name");
        JSONObject track = new JSONObject();
        track.put("title", t.get("name"));
        track.put("artist", artist);
        track.put("duration", t.get("duration_ms"));
        return track;
    }

    /**
     * Uses the link provided by a user to extract the relevant information from a public Spotify playlist
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
        GetPlaylistsItemsRequest getPlaylistsItemsRequest = spotifyApi.getPlaylistsItems(playlistId).build();
        JSONParser parser = new JSONParser();
        JSONObject original;
        try {
            original = (JSONObject) parser.parse(getPlaylistsItemsRequest.getJson());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return new JSONObject();
        }
        JSONArray arr = (JSONArray) original.get("items");
        JSONArray tracks = new JSONArray();
        for (Object o : arr) {
            tracks.add(populateTrack((JSONObject) o));
        }
        JSONObject result = new JSONObject();
        result.put("songs", tracks);
        return result;
    }

    @Override
    public String getName() {
        return "Spotify";
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