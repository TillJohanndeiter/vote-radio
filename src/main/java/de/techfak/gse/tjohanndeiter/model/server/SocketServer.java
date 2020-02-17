package de.techfak.gse.tjohanndeiter.model.server;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoWSD;

import java.io.IOException;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


public class SocketServer extends NanoWSD.WebSocket {

    public static final String CHANGED_PLAYLIST = "plyCh";
    public static final String CHANGED_SONG = "songCh";
    private static final String DEFAULT_MESSAGE_SOCKET = "nth";

    private Queue<String> queue = new PriorityQueue<>();
    private List<SocketServer> sockets;

    SocketServer(final NanoHTTPD.IHTTPSession handshakeRequest, final List<SocketServer> sockets) {
        super(handshakeRequest);
        this.sockets = sockets;
    }

    void setNextSocketMessage(final String nextSocketMessage) {
        if (!queue.contains(nextSocketMessage)) {
            queue.add(nextSocketMessage);
        }
    }

    @Override
    protected void onOpen() {
        try {
            send(DEFAULT_MESSAGE_SOCKET);
            queue.add(CHANGED_SONG);
            queue.add(CHANGED_PLAYLIST);
        } catch (IOException e) {
            e.printStackTrace();  //NOPMD
        }
        sockets.add(this);
    }


    @Override
    protected void onClose(final NanoWSD.WebSocketFrame.CloseCode closeCode, final String s, final boolean b) {
        sockets.remove(this);
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

