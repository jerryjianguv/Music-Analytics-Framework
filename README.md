## Music Analytics Framework

Our framework aims to help users of various music services better analyze their playlists. Our framework allows users to input their playlist of choice and choose a visualization for their playlist. They can choose to analyze the frequent artists in their playlist, the duration of the songs in their playlist, or the sentiment of the songs in their playlist.

### Starting the framework

In one terminal, navigate to the `back-end` directory. Run `mvn exec:exec`.

In another terminal, navigate to the `front-end` directory. Run `npm start`.

You can now open the webpage at http://localhost:3000/.

### How to Input Playlists

Get the link (or path if it is a local CSV file) of the playlist you'd like to analyze. Copy-paste it into the corresponding text box and click the button with the name of the service you've chosen to use. You can now click a visualization, but note that it can take some time to process big playlists.

### Use Examples:

* Spotify:
1. https://open.spotify.com/playlist/70ENNGL9xnJHH9W0Pkil6I (2 songs)
2. https://open.spotify.com/playlist/78GIADzJDfiuf9Sg74Em94 (10 songs)
3. https://open.spotify.com/playlist/54KFknjhHyqwic6GTmboZl (45 songs)

* Deezer:
1. https://www.deezer.com/us/playlist/10190700002 (14 songs)
2. https://www.deezer.com/us/playlist/10129935642 (29 songs)

* Local file:
Create a local CSV file with the format `Title, Artist, Duration` such as `Happy Man, Jungle, 191000`. The CSV file should *NOT* have a header.

### Framework
The `FrameworkImpl` class implements the methods required for the framework. The analysis of the playlist data gathered from the plugin happens in incremental steps and is wrapped in an `analyze` method which will fully analyze the playlist data.

### Plugins

There are two types of plugins, `DataPlugin` and `VisualPlugin`. A `DataPlugin` is responsible for downloading raw data from a music platform and sending the data back to the framework. The `VisualPlugin` is responsible for displaying the processed data received from the framework in a particular format.

### Data Plugins
Our three data plugins are:
1. Spotify Music
2. Deezer
3. Local File

The first two plugins use their respective APIs to make requests for data on public playlists. The third plugin parses a local CSV file. Each line in the local CSV file must be in the following format:

`Title, Artist, Duration`

and there is no header line.

Our data plugins transform the data they retrieve into a uniform `JSONObject` sent to the framework. We tried to keep the number of necessary methods as low as possible to allow for easy extensibility. Since most of our plugins would need to talk with other third-party libraries, it's easier to have fewer methods that the plugins need to implement to be able to communicate with the framework.

### Extensibility for Data Plugins

Many possible data plugins could be implemented to work with our framework. For example:
1. YouTube Music
2. Amazon Music
3. Pandora
4. JSON formatted local files
5. Plain text local files

These are just a select few of several other music services and local file representations that could be implemented.

### Visual Plugins
Our three visualization plugins are:
1. A bar chart to visualize the distribution of song duration in the playlist
2. A pie chart to visualize the distribution of artists in the playlist
3. A parallel coordinates chart to visualize individual songs across both duration and sentiment

To implement our charts, we used a third-party library called `EChart` which provided us with many options for the charts to use. We also thought of using a venn diagram to display sentiment, but `EChart` did not have any Venn diagram functionality and we couldn't find another library that provided us with the same level of robustness and ease of use as `EChart`. We decided to stick with `EChart` for all three of our visualizations since it provided the best level of functionality.

### Extensibility for Visual Plugins

Many possible visual plugins could be implemented to work with our framework. For example:
1. Venn diagram to represent sentiment groups
2. Sublists of the original playlist split by sentiment
3. Sublists of the original playlist split by artist

These are just a select few of several other data visualizations that could be implemented to work with the data from our framework.

### Data Processing

We used an NLP/Machine Learning API called `StanfordCoreNLP` to process the sentiment of the songs in the passed in playlist. Each song was labeled as:
1. Positive -- if the song was deemed positive/happy
2. Negative -- if the song was deemed negative/sad
3. Neutral -- if the song was deemed neither happy nor sad OR if a determination couldn't be made



