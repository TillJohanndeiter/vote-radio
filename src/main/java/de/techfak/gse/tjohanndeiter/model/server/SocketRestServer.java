package de.techfak.gse.tjohanndeiter.model.server;

import de.techfak.gse.tjohanndeiter.model.exception.shutdown.RestServerException;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import fi.iki.elonen.NanoWSD;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;


public class SocketRestServer extends NanoWSD implements RestServer, PropertyChangeListener {

    private SessionHandler sessionHandler;
    private UserManger userManger;
    private ArrayList<ServerSocket> sockets = new ArrayList<>();


    //TODO: May wrap address and port into own class or look for classes in standard library
    public SocketRestServer(final String address, final int port, final SessionHandler sessionHandler,
                            final UserManger userManger) {
        super(port);
        this.sessionHandler = sessionHandler;
        this.userManger = userManger;
    }


    @Override
    protected WebSocket openWebSocket(final IHTTPSession ihttpSession) {
        return new ServerSocket(ihttpSession, sockets, userManger);
    }


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
            throw new RestServerException("Rest-Server failed", e);
        }
    }

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

    private void setNextMessageForAllSockets(final String message) {
        for (final ServerSocket socket : sockets) {
            socket.setNextSocketMessage(message);
        }
    }

}

