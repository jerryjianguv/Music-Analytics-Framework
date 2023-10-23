package cs.cmu.cs214;

import java.util.ArrayList;
import java.util.Map;

import javafx.util.Pair;

public class ProcessedData {
    private ArrayList<Song> original;
    private Map<String, Integer> artists;
    private Map<Pair<Integer, Integer>, ArrayList<Long>> durations;
    private Map<String, ArrayList<Song>> sentiments;
    private Map<Song, String> songSentiments;

    /**
     * Creates a new {@link ProcessedData} instance.
     * 
     * @param original The original playlist passed in for parsing
     * @param artists The mapping between artist and the number of times they appear in the playlist
     * @param durations The mapping between ranges of song durations and the songs within that range in the playlist
     * @param sentiments The mapping between the type of sentiment and the songs with that sentiment
     * @param songSentiments The mapping between a song and its sentiment (reverse of the one above)
     */
    public ProcessedData(ArrayList<Song> original, Map<String, Integer> artists, Map<Pair<Integer, Integer>, ArrayList<Long>> durations,
                        Map<String, ArrayList<Song>> sentiments, Map<Song, String> songSentiments) {
        this.original = original;
        this.artists = artists;
        this.durations = durations;
        this.sentiments = sentiments;
        this.songSentiments = songSentiments;
    }

    /**
     * @return The original playlist which was processed
     */
    public ArrayList<Song> getOriginalPlaylist() {
        return this.original;
    }

    /**
     * @return The mapping between an artist and the number of times they appear in the playlist
     */
    public Map<String, Integer> getArtistFrequencies() {
        return this.artists;
    }

    /**
     * The key of an entry in the map is a Pair<Integer, Integer>. This pair represents the minute ranges.
     * For example: Pair(1, 2) is the key for songs that are between 1 and 2 minutes in length.
     * 
     * The value of an entry in the map is an ArrayList<Long>. This array contains the exact durations
     * of the songs that fall within the range of the corresponding key.
     * For example: if there were two songs with duration 1:12 and 1:59 the full map entry would be:
     * 
     * key: Pair(1, 2)
     * value: [72, 119]
     * 
     * @return The mapping between a time range and the songs whose duration falls within that range
     */
    public Map<Pair<Integer, Integer>, ArrayList<Long>> getSongDurationCateogries() {
        return this.durations;
    }

    /**
     * The key of an entry is a String. This string can be one of three:
     * "Positive" -- for positive/happy songs
     * "Neutral" -- for netural songs or songs whose sentiment could not be determined
     * "Negative" -- for negative/sad songs
     * 
     * The value of an entry in the map is an ArrayList<Song>. This array contains the songs
     * which were analyzed to be in this sentiment
     * 
     * @return The mapping between a sentiment and the songs which fall under that sentiment
     */
    public Map<String, ArrayList<Song>> getSentimentCategories() {
        return this.sentiments;
    }

    /**
     * The key of an entry is a Song.
     * 
     * The value of an entry is a String. This string corresponds to the sentiment which
     * the song was analyzed to be.
     * 
     * This mapping can be thought of as a sort of "inverse" to the mapping created
     * by {@link getSentimentCategories()}
     * 
     * @return The mapping between a song and its sentiment
     */
    public Map<Song, String> getSongSentiment() {
        return this.songSentiments;
    }
}
