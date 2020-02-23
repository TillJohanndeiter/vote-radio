package de.techfak.gse.tjohanndeiter.model.client;

import de.techfak.gse.tjohanndeiter.model.player.TimeBean;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.server.ServerSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;


public class SocketStrategy extends WebSocketClient implements RequesterStrategy {


    private static final String REGISTER_MESSAGE = "reg";
    private static final int WAIT_INTERVAL = 50;

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
        try {
            closeBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(final ServerHandshake serverHandshake) {
        send(REGISTER_MESSAGE);
    }

    @Override
    public void onMessage(final String message) {
        try {
            if (message.equals(ServerSocket.CHANGED_PLAYLIST)) {
                final VoteList voteList = httpRequester.getPlaylist();
                support.firePropertyChange(Playlist.PLAYLIST_CHANGE, null, voteList);
            } else if (message.equals(ServerSocket.INIT_USER)) {
                support.firePropertyChange(Client.USER_INIT, null, httpRequester.getUser());
            }
            else if (message.equals(ServerSocket.CHANGED_SONG)) {
                fireNewSong();

            }
        } catch (IOException | InterruptedException e) {
            support.firePropertyChange(JSON_ERROR, REGISTER_MESSAGE, null);
            e.printStackTrace();
        }
        answer(message);
    }

    private void fireNewSong() throws IOException, InterruptedException {
        final QueueSong queueSong = httpRequester.getCurrentSong();
        final TimeBean timeBean = httpRequester.getPlayedTime();
        ServerResponse response = new ServerResponse(queueSong, timeBean);
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
