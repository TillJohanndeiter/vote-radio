package de.techfak.gse.tjohanndeiter.model.server;

import de.techfak.gse.tjohanndeiter.model.exception.shutdown.RestServerException;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import fi.iki.elonen.NanoWSD;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;


public class SocketRestServer extends NanoWSD implements RestServer, PropertyChangeListener {

    private SessionHandler sessionHandler;
    private ArrayList<SocketServer> sockets = new ArrayList<>();


    public SocketRestServer(final String address, final int port, final SessionHandler sessionHandler) {
        super(address, port);
        this.sessionHandler = sessionHandler;
        sessionHandler.getVoteList().addPropertyChangeListener(this);
    }


    @Override
    protected WebSocket openWebSocket(final IHTTPSession ihttpSession) {
        return new SocketServer(ihttpSession, sockets);
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
            case VoteList.VOTE_CHANGED:
                setNextMessageForAllSockets(SocketServer.CHANGED_PLAYLIST);
                break;
            case Playlist.NEW_SONG:
                setNextMessageForAllSockets(SocketServer.CHANGED_SONG);
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
        for (final SocketServer socket : sockets) {
            socket.setNextSocketMessage(message);
        }
    }

}

