package cs.cmu.cs214;

import static org.junit.Assert.assertEquals;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import cs.cmu.cs214.plugin.data.LocalFilePlugin;

public class LocalFileTest {
    LocalFilePlugin local;

    @Before
    public void setup() {
        local = new LocalFilePlugin();
    }

    @Test
    public void testDownloadRawPlayList2Songs() throws Exception {
        JSONObject actual = local.downloadRawPlayList("src/test/java/cs/cmu/cs214/testLocalFilePlugin.csv");
        String expectedjson = """
            {"songs":[{"duration":200000,"artist":"Bob Marley","title":"Happy Man"}]}""";
        assertEquals(expectedjson, actual.toString());
    }
}
