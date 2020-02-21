package de.techfak.gse.tjohanndeiter.model.server;

import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

class SessionHandlerITest {


    private static final String LOCALHOST = "127.0.0.1";//NOPMD


    private Song song = new Song("test", "test", "test", "test", "test", 3);
    private VoteList voteList = new VoteList(List.of(new QueueSong(song, 1,0)));
    private ModelConnector modelObserver = new ModelConnector(null);
    private PollingRestServer pollingServer = new PollingRestServer(LOCALHOST, 8080,
            new SessionHandler(null,null, null, modelObserver));

    @BeforeEach
    private void startServer() throws IOException { //NOPMD
        pollingServer.start();
        voteList.addPropertyChangeListener(modelObserver);

    }

    @AfterEach
    private void endServer() { //NOPMD
        pollingServer.closeAllConnections();
    }


    /**
     * Test for User Story 13 1. Um den/die Fehler eines falschen HTTP Statuscode, ausgelöst bspw durch refactoring der
     * serve Methode zu finden, wurde die Äquivalenzklasse des korrekten HTTP GET Request gebildet. Als Repräsentant
     * wurde der GET Request "http://localhost:8080/current-song" gewählt
     */
    @Test
    void currentSongStatusCode() throws IOException {
        int code = getCode("current-song");
        Assertions.assertThat(code == 200).isTrue();
    }


    @Test
    void wrongCurrentSongRequestCode() throws IOException {
        int code = getCode("curren-song");
        Assertions.assertThat(code != 200).isTrue();
    }


    @Test
    void playlistStatusCode() throws IOException {
        int code = getCode("playlist");
        Assertions.assertThat(code == 200).isTrue();
    }


    @Test
    void wrongPlaylistRequestStatusCode() throws IOException {

        int code = getCode("plalist");
        Assertions.assertThat(code != 200).isTrue();
    }


    private int getCode(final String subAddress) throws IOException {
        final URL url = new URL("http://localhost:8080/" + subAddress);
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        return con.getResponseCode();
    }
}
