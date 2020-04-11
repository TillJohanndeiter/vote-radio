package de.tjohanndeiter.mode.client;

import de.tjohanndeiter.json.JsonException;
import de.tjohanndeiter.mode.server.CurrentSong;
import de.tjohanndeiter.mode.server.ServerSocket;
import de.tjohanndeiter.model.playlist.Playlist;
import de.tjohanndeiter.model.playlist.VoteList;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;


/**
 * Implementation of {@link UpdateStrategy}. Uses websocket communication to recognize when changes occurred in
 * playlist. Uses {@link WebSocketClient} for connection.
 */
public class SocketStrategy extends WebSocketClient implements UpdateStrategy {


    private static final String REGISTER_MESSAGE = "reg";
    private static final int WAIT_INTERVAL = 50;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final HttpRequester httpRequester;

    SocketStrategy(final HttpRequester httpRequester) throws URISyntaxException {
        super(new URI("ws://" + httpRequester.getRestAddress() + ':' + httpRequester.getPort()));
        super.connect();
        this.httpRequester = httpRequester;
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener observer) {
        support.addPropertyChangeListener(observer);
    }


    @Override
    public void stop() {
        closeConnection(0, "exit");
    }

    @Override
    public void onOpen(final ServerHandshake serverHandshake) {
        send(REGISTER_MESSAGE);
    }

    @Override
    public void onMessage(final String message) {
        try {
            switch (message) {
                case ServerSocket.CHANGED_PLAYLIST:
                    final VoteList voteList = httpRequester.getPlaylist();
                    support.firePropertyChange(Playlist.PLAYLIST_CHANGE, null, voteList);
                    break;
                case ServerSocket.INIT_USER:
                    support.firePropertyChange(Client.USER_INIT, null, httpRequester.getUser());
                    break;
                case ServerSocket.CHANGED_SONG:
                    fireNewSong();
                    break;
                default:
                    break;
            }
        } catch (IOException | InterruptedException | JsonException e) {
            support.firePropertyChange(JSON_ERROR, null, REGISTER_MESSAGE);
            e.printStackTrace(); //NOPMD
        }
        answer(message);
    }

    private void fireNewSong() throws IOException, InterruptedException, JsonException {
        final CurrentSong currentSongResponse = httpRequester.getCurrentSong();
        final Thread thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace(); //NOPMD
            }
            support.firePropertyChange(Client.NEW_SONG, null, currentSongResponse);
        });
        thread.start();
    }

    private void answer(final String message) {
        try {
            Thread.sleep(WAIT_INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace(); //NOPMD
        }

        if (isOpen()) {
            send(REGISTER_MESSAGE + message);
        }
    }

    @Override
    public void onClose(final int i, final String s, final boolean b) {
        support.firePropertyChange(Client.CANCELED_CONNECTION, null, null);
    }

    @Override
    public void onError(final Exception e) {
        e.printStackTrace();  //NOPMD
    }
}
