package cs.cmu.cs214;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.json.simple.JSONObject;

import cs.cmu.cs214.plugin.data.SpotifyPlugin;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;

public class SpotifyPluginTest {
    
    SpotifyPlugin spotify;

    @Before
    public void setup() {
        spotify = new SpotifyPlugin();
    }

    private void testDownloadRawPlayList(String link, String json, String expectedjson) throws Exception {
        // 1 - create mock object
        GetPlaylistsItemsRequest mockedRequest = mock(GetPlaylistsItemsRequest.class);
        // 2 - define stubbing return value before actual execution
        when(mockedRequest.getJson()).thenReturn(json);
        // 3 - call methods
        JSONObject actual = spotify.downloadRawPlayList(link);
        assertEquals(expectedjson, actual.toString());
    }

    @Test
    public void testDownloadRawPlayList2Songs() throws Exception {
        testDownloadRawPlayList("https://open.spotify.com/playlist/70ENNGL9xnJHH9W0Pkil6I",
                """
                {"next":null,"total":2,"offset":0,"previous":null,"limit":100,"href":"https://api.spotify.com/v1/playlists/70ENNGL9xnJHH9W0Pkil6I/tracks?offset=0&limit=100","items":[{"video_thumbnail":{"url":null},"added_at":"2023-04-17T02:47:33Z","added_by":{"href":"https://api.spotify.com/v1/users/iuz1wulm4nkmoaes34pjoddjr","id":"iuz1wulm4nkmoaes34pjoddjr","type":"user","external_urls":{"spotify":"https://open.spotify.com/user/iuz1wulm4nkmoaes34pjoddjr"},"uri":"spotify:user:iuz1wulm4nkmoaes34pjoddjr"},"is_local":false,"primary_color":null,"track":{"disc_number":1,"album":{"images":[{"width":640,"url":"https://i.scdn.co/image/ab67616d0000b2730f98789111606f77a27d6f67","height":640},{"width":300,"url":"https://i.scdn.co/image/ab67616d00001e020f98789111606f77a27d6f67","height":300},{"width":64,"url":"https://i.scdn.co/image/ab67616d000048510f98789111606f77a27d6f67","height":64}],"available_markets":["AD","AE","AG","AL","AM","AO","AR","AT","AU","AZ","BA","BB","BD","BE","BF","BG","BH","BI","BJ","BN","BO","BR","BS","BT","BW","BY","BZ","CA","CD","CG","CH","CI","CL","CM","CO","CR","CV","CW","CY","CZ","DE","DJ","DK","DM","DO","DZ","EC","EE","EG","ES","FI","FJ","FM","FR","GA","GB","GD","GE","GH","GM","GN","GQ","GR","GT","GW","GY","HK","HN","HR","HT","HU","ID","IE","IL","IN","IQ","IS","IT","JM","JO","KE","KG","KH","KI","KM","KN","KR","KW","KZ","LA","LB","LC","LI","LK","LR","LS","LT","LU","LV","LY","MA","MC","MD","ME","MG","MH","MK","ML","MN","MO","MR","MT","MU","MV","MW","MX","MY","MZ","NA","NE","NG","NI","NL","NO","NP","NR","NZ","OM","PA","PE","PG","PH","PK","PL","PS","PT","PW","PY","QA","RO","RS","RW","SA","SB","SC","SE","SG","SI","SK","SL","SM","SN","SR","ST","SV","SZ","TD","TG","TH","TJ","TL","TN","TO","TR","TT","TV","TW","TZ","UA","UG","US","UY","UZ","VC","VE","VN","VU","WS","XK","ZA","ZM","ZW"],"release_date_precision":"year","album_group":"album","type":"album","uri":"spotify:album:4pBJRxtR5TQe8hfsUgZ1r2","total_tracks":9,"is_playable":true,"artists":[{"name":"Bobby Caldwell","href":"https://api.spotify.com/v1/artists/4V4Z3qMCwYofWHtip6ePF6","id":"4V4Z3qMCwYofWHtip6ePF6","type":"artist","external_urls":{"spotify":"https://open.spotify.com/artist/4V4Z3qMCwYofWHtip6ePF6"},"uri":"spotify:artist:4V4Z3qMCwYofWHtip6ePF6"}],"release_date":"1978","name":"What You Won't Do for Love","album_type":"album","href":"https://api.spotify.com/v1/albums/4pBJRxtR5TQe8hfsUgZ1r2","id":"4pBJRxtR5TQe8hfsUgZ1r2","external_urls":{"spotify":"https://open.spotify.com/album/4pBJRxtR5TQe8hfsUgZ1r2"}},"available_markets":["AR","AU","AT","BE","BO","BR","BG","CA","CL","CO","CR","CY","CZ","DK","DO","DE","EC","EE","SV","FI","FR","GR","GT","HN","HK","HU","IS","IE","IT","LV","LT","LU","MY","MT","MX","NL","NZ","NI","NO","PA","PY","PE","PH","PL","PT","SG","SK","ES","SE","CH","TW","TR","UY","US","GB","AD","LI","MC","ID","TH","VN","RO","IL","ZA","SA","AE","BH","QA","OM","KW","EG","MA","DZ","TN","LB","JO","PS","IN","BY","KZ","MD","UA","AL","BA","HR","ME","MK","RS","SI","KR","BD","PK","LK","GH","KE","NG","TZ","UG","AG","AM","BS","BB","BZ","BT","BW","BF","CV","CW","DM","FJ","GM","GE","GD","GW","GY","HT","JM","KI","LS","LR","MW","MV","ML","MH","FM","NA","NR","NE","PW","PG","WS","SM","ST","SN","SC","SL","SB","KN","LC","VC","SR","TL","TO","TT","TV","VU","AZ","BN","BI","KH","CM","TD","KM","GQ","SZ","GA","GN","KG","LA","MO","MR","MN","NP","RW","TG","UZ","ZW","BJ","MG","MU","MZ","AO","CI","DJ","ZM","CD","CG","IQ","LY","TJ","VE","ET","XK"],"episode":false,"type":"track","external_ids":{"isrc":"USA370532331"},"uri":"spotify:track:6Dk5fHTvH897XrVzCO64Mx","duration_ms":286493,"explicit":false,"artists":[{"name":"Bobby Caldwell","href":"https://api.spotify.com/v1/artists/4V4Z3qMCwYofWHtip6ePF6","id":"4V4Z3qMCwYofWHtip6ePF6","type":"artist","external_urls":{"spotify":"https://open.spotify.com/artist/4V4Z3qMCwYofWHtip6ePF6"},"uri":"spotify:artist:4V4Z3qMCwYofWHtip6ePF6"}],"preview_url":"https://p.scdn.co/mp3-preview/506f4b71189caddd52150d8a7f4f0b1cfc23ef06?cid=5d067854c04c473881c2660cfc71e1ca","popularity":79,"name":"What You Won't Do for Love","track_number":6,"href":"https://api.spotify.com/v1/tracks/6Dk5fHTvH897XrVzCO64Mx","id":"6Dk5fHTvH897XrVzCO64Mx","is_local":false,"track":true,"external_urls":{"spotify":"https://open.spotify.com/track/6Dk5fHTvH897XrVzCO64Mx"}}},{"video_thumbnail":{"url":null},"added_at":"2023-04-17T02:47:35Z","added_by":{"href":"https://api.spotify.com/v1/users/iuz1wulm4nkmoaes34pjoddjr","id":"iuz1wulm4nkmoaes34pjoddjr","type":"user","external_urls":{"spotify":"https://open.spotify.com/user/iuz1wulm4nkmoaes34pjoddjr"},"uri":"spotify:user:iuz1wulm4nkmoaes34pjoddjr"},"is_local":false,"primary_color":null,"track":{"disc_number":1,"album":{"images":[{"width":640,"url":"https://i.scdn.co/image/ab67616d0000b2731c40418d1c37d727e8e91b04","height":640},{"width":300,"url":"https://i.scdn.co/image/ab67616d00001e021c40418d1c37d727e8e91b04","height":300},{"width":64,"url":"https://i.scdn.co/image/ab67616d000048511c40418d1c37d727e8e91b04","height":64}],"available_markets":["AD","AE","AL","AM","AO","AR","AT","AU","AZ","BA","BD","BE","BF","BG","BH","BI","BJ","BN","BO","BR","BT","BW","BY","BZ","CA","CD","CG","CH","CI","CL","CM","CO","CR","CV","CW","CY","CZ","DE","DJ","DK","DZ","EC","EE","EG","ES","FI","FJ","FM","FR","GA","GB","GE","GH","GM","GN","GQ","GR","GT","GW","GY","HK","HN","HR","HU","ID","IE","IL","IN","IQ","IS","IT","JO","JP","KE","KG","KH","KI","KM","KR","KW","KZ","LA","LB","LI","LK","LR","LS","LT","LU","LV","LY","MA","MC","MD","ME","MG","MH","MK","ML","MN","MO","MR","MT","MU","MV","MW","MX","MY","MZ","NA","NE","NG","NI","NL","NO","NP","NR","NZ","OM","PA","PE","PG","PH","PK","PL","PS","PT","PW","PY","QA","RO","RS","RW","SA","SB","SC","SE","SG","SI","SK","SL","SM","SN","SR","ST","SV","SZ","TD","TG","TH","TJ","TL","TN","TO","TR","TV","TW","TZ","UA","UG","US","UY","UZ","VE","VN","VU","WS","XK","ZA","ZM","ZW"],"release_date_precision":"year","album_group":"album","type":"album","uri":"spotify:album:321q9p7PELvzcFAWxml7VX","total_tracks":12,"is_playable":true,"artists":[{"name":"Bob Marley & The Wailers","href":"https://api.spotify.com/v1/artists/2QsynagSdAqZj3U9HgDzjD","id":"2QsynagSdAqZj3U9HgDzjD","type":"artist","external_urls":{"spotify":"https://open.spotify.com/artist/2QsynagSdAqZj3U9HgDzjD"},"uri":"spotify:artist:2QsynagSdAqZj3U9HgDzjD"}],"release_date":"1980","name":"Uprising","album_type":"album","href":"https://api.spotify.com/v1/albums/321q9p7PELvzcFAWxml7VX","id":"321q9p7PELvzcFAWxml7VX","external_urls":{"spotify":"https://open.spotify.com/album/321q9p7PELvzcFAWxml7VX"}},"available_markets":["AR","AU","AT","BE","BO","BR","BG","CA","CL","CO","CR","CY","CZ","DK","DE","EC","EE","SV","FI","FR","GR","GT","HN","HK","HU","IS","IE","IT","LV","LT","LU","MY","MT","MX","NL","NZ","NI","NO","PA","PY","PE","PH","PL","PT","SG","SK","ES","SE","CH","TW","TR","UY","US","GB","AD","LI","MC","ID","JP","TH","VN","RO","IL","ZA","SA","AE","BH","QA","OM","KW","EG","MA","DZ","TN","LB","JO","PS","IN","BY","KZ","MD","UA","AL","BA","HR","ME","MK","RS","SI","KR","BD","PK","LK","GH","KE","NG","TZ","UG","AM","BZ","BT","BW","BF","CV","CW","FJ","GM","GE","GW","GY","KI","LS","LR","MW","MV","ML","MH","FM","NA","NR","NE","PW","PG","WS","SM","ST","SN","SC","SL","SB","SR","TL","TO","TV","VU","AZ","BN","BI","KH","CM","TD","KM","GQ","SZ","GA","GN","KG","LA","MO","MR","MN","NP","RW","TG","UZ","ZW","BJ","MG","MU","MZ","AO","CI","DJ","ZM","CD","CG","IQ","LY","TJ","VE","ET","XK"],"episode":false,"type":"track","external_ids":{"isrc":"USIR28000016"},"uri":"spotify:track:5O4erNlJ74PIF6kGol1ZrC","duration_ms":237000,"explicit":false,"artists":[{"name":"Bob Marley & The Wailers","href":"https://api.spotify.com/v1/artists/2QsynagSdAqZj3U9HgDzjD","id":"2QsynagSdAqZj3U9HgDzjD","type":"artist","external_urls":{"spotify":"https://open.spotify.com/artist/2QsynagSdAqZj3U9HgDzjD"},"uri":"spotify:artist:2QsynagSdAqZj3U9HgDzjD"}],"preview_url":null,"popularity":82,"name":"Could You Be Loved","track_number":8,"href":"https://api.spotify.com/v1/tracks/5O4erNlJ74PIF6kGol1ZrC","id":"5O4erNlJ74PIF6kGol1ZrC","is_local":false,"track":true,"external_urls":{"spotify":"https://open.spotify.com/track/5O4erNlJ74PIF6kGol1ZrC"}}}]}
                """,
                """
                {"songs":[{"duration":286493,"artist":"Bobby Caldwell","title":"What You Won't Do for Love"},{"duration":237000,"artist":"Bob Marley & The Wailers","title":"Could You Be Loved"}]}""");
    }

    /*
    @Test
    public void testDownloadRawPlayList45Songs() {
        System.out.println(spot.downloadRawPlayList("https://open.spotify.com/playlist/54KFknjhHyqwic6GTmboZl"));
    }
    */
}
