package de.techfak.gse.tjohanndeiter.model.server;

import de.techfak.gse.tjohanndeiter.model.exception.client.UserDoesntExits;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoWSD;

import java.io.IOException;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


public class ServerSocket extends NanoWSD.WebSocket {

    public static final String CHANGED_PLAYLIST = "plyCh";
    public static final String CHANGED_SONG = "songCh";
    public static final String INIT_USER = "userInit";
    private static final String DEFAULT_MESSAGE_SOCKET = "nth";

    private Queue<String> queue = new PriorityQueue<>();
    private List<ServerSocket> sockets;
    private UserManger userManger;

    ServerSocket(final NanoHTTPD.IHTTPSession handshakeRequest, final List<ServerSocket> sockets,
                 final UserManger userManger) {
        super(handshakeRequest);
        this.sockets = sockets;
        this.userManger = userManger;
    }

    void setNextSocketMessage(final String nextSocketMessage) {
        if (!queue.contains(nextSocketMessage)) {
            queue.add(nextSocketMessage);
        }
    }

    @Override
    protected void onOpen() {
        try {
            userManger.addUser(new User(super.getHandshakeRequest().getRemoteIpAddress()));
            send(DEFAULT_MESSAGE_SOCKET);
            queue.add(CHANGED_SONG);
            queue.add(CHANGED_PLAYLIST);
            queue.add(INIT_USER);
        } catch (IOException e) {
            e.printStackTrace();  //NOPMD
        }
        sockets.add(this);
    }


    @Override
    protected void onClose(final NanoWSD.WebSocketFrame.CloseCode closeCode, final String s, final boolean b) {
        try {
            userManger.removeUserByIp(super.getHandshakeRequest().getRemoteIpAddress());
            sockets.remove(this);
        } catch (final UserDoesntExits e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMessage(final NanoWSD.WebSocketFrame webSocketFrame) {
        try {
            if (queue.isEmpty()) {
                send(DEFAULT_MESSAGE_SOCKET);
            } else {
                send(queue.poll());
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
        e.printStackTrace();  //NOPMD
    }
}

