package de.techfak.gse.tjohanndeiter.model.server;

import de.techfak.gse.tjohanndeiter.model.exception.shutdown.RestServerException;

public interface RestServer {

    void startServer() throws RestServerException;

    void stop();

    int getPort();

    String getIpAddress();
}
