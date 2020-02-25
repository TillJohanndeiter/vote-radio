package de.techfak.gse.tjohanndeiter.mode.client;

import de.techfak.gse.tjohanndeiter.model.player.TimeBean;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.mode.server.ServerSocket;
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
        close();
        try {
            closeBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace(); //NOPMD
        }
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
        } catch (IOException | InterruptedException e) {
            support.firePropertyChange(JSON_ERROR, REGISTER_MESSAGE, null);
            e.printStackTrace(); //NOPMD
        }
        answer(message);
    }

    private void fireNewSong() throws IOException, InterruptedException {
        final QueueSong queueSong = httpRequester.getCurrentSong();
        final TimeBean timeBean = httpRequester.getPlayedTime();
        final ServerResponse response = new ServerResponse(queueSong, timeBean);
        final Thread thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace(); //NOPMD
            }
            support.firePropertyChange(Client.NEW_SONG, null, response);
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
        close();
    }

    @Override
    public void onError(final Exception e) {
        e.printStackTrace();  //NOPMD
    }
}
