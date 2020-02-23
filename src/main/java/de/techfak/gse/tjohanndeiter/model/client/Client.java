package de.techfak.gse.tjohanndeiter.model.client;

import de.techfak.gse.tjohanndeiter.model.player.ReceiverPlayer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * Representation of client. Includes a {@link #updateStrategy} for way to know when changed occurred on the server.
 * Use {@link #receiverPlayer} for receiving stream and {@link #httpRequester} to make http Requests to the server.
 */
public class Client {

    public static final String INVALID_URL = "invalidURL";
    public static final String INVALID_SERVER = "invalidServer";
    public static final String CONNECTED = "successfulConnection";
    public static final String CANCELED_CONNECTION = "canceledConnection";
    public static final String NEW_SONG = "newReceivedSong";
    public static final String NEW_PLAYER = "newPlayer";
    public static final String SUCCESS_VOTED = "sucVoted";
    public static final String USER_INIT = "newUser";
    private static final int STATUS_OK = 200;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final List<PropertyChangeListener> listeners = new ArrayList<>();
    private HttpRequester httpRequester;
    private UpdateStrategy updateStrategy;
    private ReceiverPlayer receiverPlayer;
    private String restAddress;
    private String port;
    private boolean connected;

    /**
     * Method for switch the state of the client between connected and disconnected.
     * @param restAddress ipAddress of server
     * @param port port of sever
     */
    public void changeConnection(final String restAddress, final String port) {
        if (connected) {
            endConnection();
        } else {
            this.restAddress = restAddress;
            this.port = port;
            connect();
        }
    }


    /**
     * Sends a vote for a song by id to server.
     * @param id id of song to vote
     */
    public void voteById(final int id) {
        try {
            final int code = httpRequester.voteById(id);
            if (code == STATUS_OK) {
                support.firePropertyChange(SUCCESS_VOTED, null, id);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); //NOPMD
        }
    }

    private void connect() {
        try {
            httpRequester = new HttpRequester(restAddress, port);
            if (httpRequester.validConnection()) {
                establishConnection();
            } else {
                connected = false;
                support.firePropertyChange(INVALID_URL, null, restAddress);
            }
        } catch (URISyntaxException | IllegalArgumentException e) {
            connected = false;
            support.firePropertyChange(INVALID_URL, null, restAddress);
        }  catch (IOException | InterruptedException e) {
            connected = false;
            support.firePropertyChange(INVALID_SERVER, null, restAddress);
        }
    }

    private void establishConnection() throws IOException, InterruptedException, URISyntaxException {
        receiverPlayer = new ReceiverPlayer(httpRequester.getMusicAddress(), httpRequester.getMusicPort());
        updateStrategy = new SocketStrategy(new HttpRequester(restAddress, port));
        addObservers();
        receiverPlayer.startPlay();
        support.firePropertyChange(NEW_PLAYER, null, receiverPlayer);
        connected = true;
        support.firePropertyChange(CONNECTED, null, restAddress);
    }

    private void addObservers() {
        for (final PropertyChangeListener listener : listeners) {
            receiverPlayer.addPropertyChangeListener(listener);
            updateStrategy.addPropertyChangeListener(listener);
        }
    }

    public void addPropertyChangeListener(final PropertyChangeListener observer) {
        support.addPropertyChangeListener(observer);
        listeners.add(observer);
    }

    /**
     * End connection for {@link #updateStrategy} and music stream  {@link #receiverPlayer}.
     */
    public void endConnection() {
        if (receiverPlayer != null) {
            receiverPlayer.stop();
        }
        if (updateStrategy != null) {
            updateStrategy.stop();
        }
        connected = false;
        support.firePropertyChange(CANCELED_CONNECTION, false, true);
    }

    public void kill() {
        endConnection();
        receiverPlayer.end();
    }

    public ReceiverPlayer getReceiverPlayer() {
        return receiverPlayer;
    }
}
