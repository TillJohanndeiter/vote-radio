package de.techfak.gse.tjohanndeiter.model.server;

import de.techfak.gse.tjohanndeiter.model.exception.shutdown.RestServerException;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

/**
 * Server without socket structure. But better for testing especially {@link SessionHandler}.
 * @deprecated because sockets are way better than polling
 */
@Deprecated
public class PollingRestServer extends NanoHTTPD implements RestServer {


    private SessionHandler sessionHandler;

    PollingRestServer(final String address, final int restPort, final SessionHandler sessionHandler) {
        super(address, restPort);
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Response serve(final IHTTPSession session) {
        return sessionHandler.serve(session);
    }

    @Override
    public void startServer() throws RestServerException {
        try {
            super.start();
        } catch (IOException e) {
            throw new RestServerException("Start of Rest Server Failed. Maybe Port is use", e);
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
}
