package cs.cmu.cs214.plugin.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import cs.cmu.cs214.framework.core.DataPlugin;
import cs.cmu.cs214.framework.core.Framework;

public class FakeDataPlugin implements DataPlugin {
    private String serviceName = "Fake Data Plugin";
    private Framework framework;

    public static final int DURATIONSHORT = 1200000;
    public static final int DURATIONLONG = 1800000;
    
    @Override
    public JSONObject downloadRawPlayList(String link) {
        // create raw playlist (json object) without external calls to API
        JSONObject dummyRawPlaylist = new JSONObject();
        dummyRawPlaylist.put("username", "imposter");
        dummyRawPlaylist.put("playlist_id","2");
        JSONArray songArray = new JSONArray();
        
        JSONObject happySong = new JSONObject();
        happySong.put("title", "I am so happy");
        happySong.put("duration", (long)DURATIONSHORT);
        happySong.put("artist", "Dave Eckhardt");
        songArray.add(happySong);
        JSONObject sadSong = new JSONObject();
        sadSong.put("title", "I am so sad");
        sadSong.put("duration", (long)DURATIONLONG);
        sadSong.put("artist", "Andy Pavlo");
        songArray.add(sadSong);
        
        dummyRawPlaylist.put("songs", songArray);
        return dummyRawPlaylist;
    }

    @Override
    public String getName() {
        return serviceName;
    }

    @Override
    public void onRegister(Framework framework) {
        this.framework = framework;
    }

    @Override
    public void onFinished() {
    }
}
