package de.techfak.gse.tjohanndeiter.model.server;

import de.techfak.gse.tjohanndeiter.model.exception.shutdown.RestServerException;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import fi.iki.elonen.NanoWSD;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * {@link RestServer} with socket support for better communication with client.
 */
public class SocketRestServer extends NanoWSD implements RestServer, PropertyChangeListener {

    private SessionHandler sessionHandler;
    private UserManger userManger;
    private ArrayList<ServerSocket> allConnectedSockets = new ArrayList<>();


    /**
     * Initialize server at #port.
     * @param port port of server
     * @param sessionHandler handel http requests
     * @param userManger manages
     */
    public SocketRestServer(final int port, final SessionHandler sessionHandler,
                            final UserManger userManger) {
        super(port);
        this.sessionHandler = sessionHandler;
        this.userManger = userManger;
    }


    /**
     *
     * @param ihttpSession of new socket connection
     * @return #ServerSocket for new connection
     */
    @Override
    protected WebSocket openWebSocket(final IHTTPSession ihttpSession) {
        return new ServerSocket(ihttpSession, allConnectedSockets, userManger);
    }


    /**
     * Use {@link #sessionHandler} for normal requests and serve form {@link NanoWSD} for websocket requests.
     * @param session incoming session form client
     * @return response
     */
    @Override
    public Response serve(final IHTTPSession session) {
        if (isWebsocketRequested(session)) {
            return super.serve(session);
        } else {
            return sessionHandler.serve(session);
        }
    }

    @Override
    public void startServer() throws RestServerException {
        try {
            start();
        } catch (IOException e) {
            throw new RestServerException("Rest-Server initialization failed", e);
        }
    }

    /**
     * Add update messages to all sockets in {@link #allConnectedSockets}.
     * @param evt event in model
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final String name = evt.getPropertyName();

        switch (name) {
            case Playlist.PLAYLIST_CHANGE:
                setNextMessageForAllSockets(ServerSocket.CHANGED_PLAYLIST);
                break;
            case MusicPlayer.NEW_SONG:
                setNextMessageForAllSockets(ServerSocket.CHANGED_SONG);
                break;
            default:
                break;
        }
    }

    @Override
    public int getPort() {
        return super.getListeningPort();
    }

    @Override
    public String getIpAddress() {
        return super.getHostname();
    }


    /**
     * Add to all {@link ServerSocket} in {@link #allConnectedSockets} message to nextMessages.
     * @param message next message for all sockets
     */
    private void setNextMessageForAllSockets(final String message) {
        for (final ServerSocket socket : allConnectedSockets) {
            socket.setNextSocketMessage(message);
        }
    }

}

