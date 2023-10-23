package cs.cmu.cs214;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import api.deezer.http.DeezerRequest;
import api.deezer.objects.Album;
import api.deezer.objects.Artist;
import api.deezer.objects.Playlist;
import api.deezer.objects.Track;
import api.deezer.objects.data.DeezerData;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cs.cmu.cs214.plugin.data.DeezerPlugin;

public class DeezerPluginTest {
    DeezerPlugin deezer;
    
    @Before
    public void setUp() {
        deezer = new DeezerPlugin();
    }

    private void testDownloadRawPlayList(String link, Playlist fakePlaylist, String expectedjson) throws Exception {
        // 1 - create mock object
        DeezerRequest<Playlist> mockedRequest = mock(DeezerRequest.class);
        // 2 - define stubbing return value before actual execution
        when(mockedRequest.execute()).thenReturn(fakePlaylist);
        // 3 - call methods
        JSONObject actual = deezer.downloadRawPlayList(link);
        assertEquals(expectedjson, actual.toString());
    }

    @Test
    public void testDownloadRawPlayList2Songs() throws Exception {
        Playlist p = new Playlist();
        DeezerData<Track> tracks = new DeezerData<Track>();
        List<Track> data = new ArrayList<Track>();
        Track t = new Track();
        Artist art = new Artist();
        art.setName("The 2 Bears");
        t.setArtist(art);
        t.setDuration(254);
        Album a = new Album();
        a.setTitle("The 2 Bears");
        t.setAlbum(a);
        t.setTitle("Not This Time");
        data.add(t);
        tracks.setData(data);
        p.setTracks(tracks);
        testDownloadRawPlayList("https://www.deezer.com/us/playlist/1086572431",
                p,
                """
                    {"songs":[{"duration":254,"artist":"The 2 Bears","title":"Not This Time"}]}""");
    }
}
