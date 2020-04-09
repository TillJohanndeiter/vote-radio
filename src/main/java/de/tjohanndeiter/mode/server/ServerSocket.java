package de.tjohanndeiter.mode.server;

import de.tjohanndeiter.exception.client.UserDoesntExits;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoWSD;

import java.io.IOException;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


/**
 * Socket for server side. Communicate with client about changes of current song or
 * {@link de.tjohanndeiter.model.playlist.Playlist}.
 */
public class ServerSocket extends NanoWSD.WebSocket {

    public static final String CHANGED_PLAYLIST = "plyCh";
    public static final String CHANGED_SONG = "songCh";
    public static final String INIT_USER = "userInit";
    private static final String DEFAULT_MESSAGE_SOCKET = "nth";

    private final Queue<String> nextMessages = new PriorityQueue<>();
    private final List<ServerSocket> allServerSockets;
    private final UserManger userManger;

    ServerSocket(final NanoHTTPD.IHTTPSession handshakeRequest, final List<ServerSocket> allServerSockets,
                 final UserManger userManger) {
        super(handshakeRequest);
        this.allServerSockets = allServerSockets;
        this.userManger = userManger;
    }

    void setNextSocketMessage(final String nextSocketMessage) {
        if (!nextMessages.contains(nextSocketMessage)) {
            nextMessages.add(nextSocketMessage);
        }
    }


    /**
     * Add new client to user manger and set message for current sing playlist and that user
     * has been created. Also add server to {@link #allServerSockets}.
     */
    @Override
    protected void onOpen() {
        try {
            userManger.addUser(new User(super.getHandshakeRequest().getRemoteIpAddress()));
            send(DEFAULT_MESSAGE_SOCKET);
            nextMessages.add(CHANGED_SONG);
            nextMessages.add(CHANGED_PLAYLIST);
            nextMessages.add(INIT_USER);
        } catch (IOException e) {
            e.printStackTrace();  //NOPMD
        }
        allServerSockets.add(this);
    }


    /**
     * Remove user form {@link #userManger} and #ServerSocket form {@link #allServerSockets}.
     * @param closeCode close code
     * @param s message form client
     * @param b boolean
     */
    @Override
    protected void onClose(final NanoWSD.WebSocketFrame.CloseCode closeCode, final String s, final boolean b) {
        try {
            userManger.removeUserByIp(super.getHandshakeRequest().getRemoteIpAddress());
            allServerSockets.remove(this);
        } catch (final UserDoesntExits e) {
            e.printStackTrace(); //NOPMD
        }
    }

    /**
     * Answer client with next message from {@link #nextMessages}.
     * @param webSocketFrame webSocketFrame
     */
    @Override
    protected void onMessage(final NanoWSD.WebSocketFrame webSocketFrame) {
        try {
            if (nextMessages.isEmpty()) {
                send(DEFAULT_MESSAGE_SOCKET);
            } else {
                send(nextMessages.poll());
            }
        } catch (IOException e) {
            e.printStackTrace();  //NOPMD
        }
    }

    @Override
    protected void onPong(final NanoWSD.WebSocketFrame webSocketFrame) {
    }

    @Override
    protected void onException(final IOException e) {
        allServerSockets.remove(this);
        e.printStackTrace();  //NOPMD
    }
}

