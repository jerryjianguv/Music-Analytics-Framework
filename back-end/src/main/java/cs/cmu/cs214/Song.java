package cs.cmu.cs214;

public class Song {
    private String title;
    private String artist;
    private Long duration;
    
    /**
     * Creates a new {@link Song} instance.
     * 
     * @param title The title of the song
     * @param artist The artist of the song
     * @param duration The length of the song in milliseconds
     */
    public Song(String title, String artist, Long duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }
    
    public Long getDurationMS() {
        return this.duration;
    }
}
