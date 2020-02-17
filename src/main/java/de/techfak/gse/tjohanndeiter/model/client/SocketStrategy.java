package de.techfak.gse.tjohanndeiter.model.client;

import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.server.SocketServer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class SocketStrategy extends WebSocketClient implements RequesterStrategy {


    private static final String REGISTER_MESSAGE = "reg";
    private static final int WAIT_INTERVAL = 250;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private HttpRequester httpRequester;

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
    }

    @Override
    public void onOpen(final ServerHandshake serverHandshake) {
        send(REGISTER_MESSAGE);
    }

    @Override
    public void onMessage(final String message) {
        try {
            if (message.equals(SocketServer.CHANGED_PLAYLIST)) {
                final VoteList voteList = httpRequester.getPlaylist();
                support.firePropertyChange(Playlist.PLAYLIST_CHANGE, null, voteList);
            } else if (message.equals(SocketServer.CHANGED_SONG)) {
                final QueueSong queueSong = httpRequester.getCurrentSong();
                support.firePropertyChange(Playlist.NEW_SONG, null, queueSong);
            }
        } catch (IOException | InterruptedException e) {
            support.firePropertyChange(JSON_ERROR, REGISTER_MESSAGE, null);
        }
        answer(message);
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
        try {
            closeBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(final Exception e) {
        e.printStackTrace();  //NOPMD
    }
}
