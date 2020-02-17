package de.techfak.gse.tjohanndeiter.model.client;

import de.techfak.gse.tjohanndeiter.model.player.ReceiverPlayer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Client {

    public static final String INVALID_URL = "invalidURL";
    public static final String INVALID_SERVER = "invalidServer";
    public static final String CONNECTED = "successfulConnection";
    public static final String CANCELED_CONNECTION = "canceledConnection";

    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private HttpRequester httpRequester;
    private List<PropertyChangeListener> listeners = new ArrayList<>();
    private RequesterStrategy requesterStrategy;
    private ReceiverPlayer receiverPlayer;
    private String restAddress;
    private String port;
    private boolean connected;


    public void changeConnection(String restAddress, final String port) {
        if (connected) {
            endConnection();
        } else {
            this.restAddress = restAddress;
            this.port = port;
            connect();
        }
    }

    public void voteById(final int id) {
        try {
            httpRequester.voteById(id);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); //NOPMD
        }
    }

    private void connect() {
        try {
            httpRequester = new HttpRequester(restAddress, port);
            if (httpRequester.validConnection()) {
                receiverPlayer = new ReceiverPlayer(httpRequester.getMusicAddress(), httpRequester.getMusicPort());
                requesterStrategy = new SocketStrategy(new HttpRequester(restAddress, port));
                for (final PropertyChangeListener listener : listeners) {
                    receiverPlayer.addPropertyChangeListener(listener);
                    requesterStrategy.addPropertyChangeListener(listener);
                }
                receiverPlayer.startPlay();
                connected = true;
                support.firePropertyChange(CONNECTED, null, restAddress);

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

    public void addPropertyChangeListener(final PropertyChangeListener observer) {
        support.addPropertyChangeListener(observer);
        listeners.add(observer);
    }

    public void endConnection() {
        if (receiverPlayer != null) {
            receiverPlayer.stop();
        }
        if (requesterStrategy != null) {
            requesterStrategy.stop();
        }
        connected = false;
        support.firePropertyChange(CANCELED_CONNECTION, false, true);
    }
}
