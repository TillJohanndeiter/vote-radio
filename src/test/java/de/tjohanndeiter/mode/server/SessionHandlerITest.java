package de.tjohanndeiter.mode.server;

import de.tjohanndeiter.exception.shutdown.RestServerException;
import de.tjohanndeiter.model.database.Song;
import de.tjohanndeiter.model.playlist.VoteList;
import de.tjohanndeiter.model.playlist.VotedSong;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

class SessionHandlerITest {


    private final ModelConnector modelObserver = new ModelConnector(null, null, null);
    private final SocketRestServer socketRestServer = new SocketRestServer(6060,
            new SessionHandler(null, null, modelObserver), null);

    SessionHandlerITest() throws RestServerException {
        final Song song = new Song("test", "test", "test", "test", "test", 3);
        final VoteList voteList = new VoteList(List.of(new VotedSong(song, 1, 0)));
        voteList.addPropertyChangeListener(modelObserver);
    }

    @BeforeEach
    private void startServer() throws IOException { //NOPMD
            socketRestServer.start();
    }

    @AfterEach
    private void endServer() { //NOPMD
        socketRestServer.closeAllConnections();
        socketRestServer.stop();
    }


    @Test
    void wrongCurrentSongRequestCode() throws IOException {
        System.out.println("sad");
        final int code = getCode("curren-song");
        Assertions.assertThat(code != 200).isTrue();

    }


    @Test
    void playlistStatusCode() throws IOException {
        final int code = getCode("playlist");
        Assertions.assertThat(code == 200).isTrue();
        socketRestServer.closeAllConnections();

    }


    @Test
    void wrongPlaylistRequestStatusCode() throws IOException {
        final int code = getCode("plalist");
        Assertions.assertThat(code != 200).isTrue();
        socketRestServer.closeAllConnections();

    }


    private int getCode(final String subAddress) throws IOException {
        final URL url = new URL("http://localhost:6060/" + subAddress);
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        return con.getResponseCode();
    }
}
