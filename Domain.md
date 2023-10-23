### Framework Domain:

Our framework aims to help users of various music services better manage and analyze their playlists. Part of the motivation for this framework comes from our team discussing the lack of a Spotify feature to split a playlist into sublists. Spotify has the functionality to merge playlists, but there is no way to separate an already existing playlist. Additionally, Spotify does not keep two merged playlists in sync (e.g. if you add a song to one of the two merged lists, it won’t appear in the merged playlist). In other words, Spotify doesn’t implement the composite design pattern :).

Our framework will perform analysis on user playlists (provided by data plugins) and show the results in different ways (using visualization plugins). The framework will perform the analysis itself so that it provides benefits for reuse. Visual plugins will also be reusable because they will work with any new data plugin.

Data plugins could provide a JSON or CSV object with information about each song in the playlist. Data plugins could include:
1. Spotify plugin using the Spotify API - takes in a playlist from a Spotify user
2. Apple Music plugin using the Apple Music API - takes in a playlist from an Apple Music user
3. Amazon Music plugin using the Amazon Music API - takes in a playlist from an Amazon Music user

Java code exists to work with these APIs. For example:
1. [Spotify API in Java](https://github.com/spotify-web-api-java/spotify-web-api-java)
2. [Apple Music API in Java](https://github.com/japlscript/obstmusic)

We can split a playlist in multiple ways including by artist, by language, by sentiment, or by the order in which songs are added. For example, our framework could use some APIs for music sentiment analysis:
1. [MuSe](https://www.kaggle.com/datasets/cakiki/muse-the-musical-sentiment-dataset) which provides sentiment analysis for 90,000 songs
2. [Music Sentiment Analysis](https://github.com/RahulGaonkar/Music-Sentiment-Analysis)

The framework will analyze the playlists and songs obtained by the data plugins, and will give the result of the analysis to the visualization plugins. For example, the framework can send the visualization plugins a JSON object with the analyzed data. The visualization plugins will then display the data in multiple ways:
1. Display of the separated playlists
2. Bar chart with the most frequent artists in the playlist
3. Line graph displaying the change in overall sentiment of the playlist					

### Generality & Specificity:

#### Generality:

Our framework will allow for input data sources from various music services like Spotify and Apple Music as long as they have functionality for getting user playlists. Our framework will be extensible to all data sources with our grouping mechanism of music for their users. We can easily add another data plugin with input from another music service. The only requirement is that the music service provides methods to access user playlists. With the input data, our framework will be able to conduct various analyses, such as sentiment analysis, related to the user's music preferences and store the useful information in common data structures. If we had chosen to limit our framework scope to only a specific music service like Spotify, our framework would have too few extension points so that only the users of that service could use our framework.

#### Specificity:

We limit the scope of our framework only to music services that have user playlists functionality so that our framework won’t be too generic and have little reuse value. We do not venture into other entertainment formats like movie services (Netflix, Hulu, HBO Max). Our input data only comes from the playlist, so our plugin can assume some specific formats about the input. Also, we will perform analysis only on playlists instead of other data inputs, such as user comments, which restricts the scope of our framework.
### Project structure: 
This project will initially provide plugins for three music services, most likely Spotify, Apple Music, and Amazon Music. The data plugins will have functions that take in data from the service API and will transform it into a uniform format. The format in the framework will be JSON or CSV. The plugins will further parse the JSON file to APIResponse data structure that can be used by our framework. 
The plugin interfaces will be located in the main directory of the project. Two key interfaces will be `DataPlugin` and `VisualPlugin`. These plugins will be organized into two main packages: `data` and `visual`. Within each package, there will be specific plugin implementations that utilize different APIs and visualization methods. The key data structure for API responses would be a JSON object like the following:
```
{
  "message": "Mood analysis.",
  "data": {
    "playlist_name": “Play list A",
    "playlist_id": "1234",
    "sentiment_categories": {
      "sad": ["Song A"],
      "happy": ["Song B"],
      "netural": ["Song C" ]
}
```
The analyzed data will be given to the visual plugins for display purposes. The visualization will be displayed to the user via the framework user interface.
The plugins should be loaded dynamically. The framework searches for all classes within the `data` and `visual` packages. The framework checks if these classes implement either the `DataPlugin` or `VisualPlugin` interface. If a class implements the interface, then it should be initialized so it can process user input.
### Plugin Interfaces:

The main responsibility of the data plugin is to get the raw input data (playlists) from the music service API and transform (parse) the raw input into the format that the framework would require. The raw format is a JSON file provided by the music API containing all the information pertaining to the user’s playlist. The DataPlugin is in charge of parsing the raw playlist and converting it into data structure APIResponse so that the framework can directly use it. 

As for the visual plugin, its main responsibility is to take the processed data input of type ProcessedData from the framework and specify how the specific data should be displayed according to its own rules. 

```
public DataPlugin {
    String getServiceName();
    void onRegister(Framework framework);
    APIResponse download();
    JSONObject getRawPlayList();
    // life cycle methods
    void onStartInput();
    void onGetUserName();
    APIResponse onGetAPIResponse();
    void onFinish();
}

public VisualPlugin {
    String getDisplayType();
    void onRegister(Framework framework);
    void draw(ProcessedData data);
    // life cycle methods
    void onDisplay();
    void onGetInput();
    void onFinish(); 
}
```
The key data structures we are using are defined as follows. Note that for this milestone this is just a very rudimentary version, we might add more fields as we implement each structure.

```
class APIResponse {
    String userName;
    int playlistID;
    Song [] playlists;
    Time time;
}

class Song {
    String name;
    String genre;
    String artist;
    String lyrics; // use for sentiment analysis
    int length;
}

class ProcessedData {
    Song processedSong;
    Song [] originalPlaylist;	
    Song [] suggestedPlaylist;
    Map<String, List<Song>> sentimentCategories; 
    Map<Integer, List<Song> songDurations;
    // the segment list of the time duration and lists of songs in the playlist;
    Map<String, List<Song>> artistSongs; 
    // the mapping between artists and list of their songs in the playlists
}
```