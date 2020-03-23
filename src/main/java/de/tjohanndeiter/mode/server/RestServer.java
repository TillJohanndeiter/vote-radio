package de.tjohanndeiter.mode.server;

import de.tjohanndeiter.exception.shutdown.RestServerException;

/**
 * Abstract representation of a rest server.
 */
public interface RestServer {

    void startServer() throws RestServerException;

    void stop();

    int getPort();

    String getIpAddress();
}
