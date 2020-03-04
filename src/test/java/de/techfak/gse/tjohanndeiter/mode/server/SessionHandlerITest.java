package de.techfak.gse.tjohanndeiter.mode.server;

import de.techfak.gse.tjohanndeiter.exception.shutdown.RestServerException;
import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.playlist.VotedSong;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@SuppressWarnings("deprecation")
class SessionHandlerITest {


    private static final String LOCALHOST = "127.0.0.1";//NOPMD


    private final Song song = new Song("test", "test", "test", "test", "test", 3);
    private final VoteList voteList = new VoteList(List.of(new VotedSong(song, 1, 0)));
    private final ModelConnector modelObserver = new ModelConnector(null, null, null);
    private final PollingRestServer pollingServer = new PollingRestServer(LOCALHOST, 8080,
            new SessionHandler(null, null, modelObserver));

    SessionHandlerITest() throws RestServerException {
    }

    @BeforeEach
    private void startServer() throws IOException { //NOPMD
        modelObserver.setCurrentSongJson("test");
        pollingServer.start();
        voteList.addPropertyChangeListener(modelObserver);

    }

    @AfterEach
    private void endServer() { //NOPMD
        pollingServer.closeAllConnections();
    }


    @Test
    void wrongCurrentSongRequestCode() throws IOException {
        final int code = getCode("curren-song");
        Assertions.assertThat(code != 200).isTrue();
    }


    @Test
    void playlistStatusCode() throws IOException {
        final int code = getCode("playlist");
        Assertions.assertThat(code == 200).isTrue();
    }


    @Test
    void wrongPlaylistRequestStatusCode() throws IOException {

        final int code = getCode("plalist");
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
