package cs.cmu.cs214;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cs.cmu.cs214.framework.core.FrameworkImpl;
import cs.cmu.cs214.plugin.data.SpotifyPlugin;
import javafx.util.Pair;

public class FrameworkTest {
    FrameworkImpl framework;
    SpotifyPlugin spotify;
    ProcessedData data;

    @Before
    public void setup() {
        framework = new FrameworkImpl();
        spotify = new SpotifyPlugin();

        framework.analyze(spotify, "https://open.spotify.com/playlist/70ENNGL9xnJHH9W0Pkil6I");
        data = framework.getProcessedData();
    }

    @Test
    public void testSameArtists() {
        Map<String, Integer> artists = data.getArtistFrequencies();
        Set<String> actualartists = artists.keySet();

        ArrayList<String> expectedArtists = new ArrayList<>();
        expectedArtists.add("Bobby Caldwell");
        expectedArtists.add("Bob Marley & The Wailers");

        assertEquals(expectedArtists.size(), actualartists.size());
        for (int i = 0; i < expectedArtists.size(); i++) {
            assertTrue(actualartists.contains(expectedArtists.get(i)));
        }
    }

    @Test
    public void testSameDurations() {
        Map<Pair<Integer, Integer>, ArrayList<Long>> durations = data.getSongDurationCateogries();
        for (ArrayList<Long> a : durations.values()) {
            assertEquals(1, a.size());
        }
    }

    @Test
    public void testSentiments() {
        Map<String, ArrayList<Song>> sentiments = data.getSentimentCategories();
        for (String s : sentiments.keySet()) {
            assertTrue(s.equals("Neutral") || s.equals("Positive"));
            assertEquals(1, sentiments.get(s).size());
        }
    }
}
