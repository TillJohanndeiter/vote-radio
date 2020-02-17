package de.techfak.gse.tjohanndeiter.model.server;

import de.techfak.gse.tjohanndeiter.model.exception.shutdown.RestServerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class PollingRestServerTest {

    private static final String LOCALHOST = "127.0.0.1";//NOPMD

    private PollingRestServer pollingRestServer;

    @BeforeEach
    void setUp() {
        pollingRestServer = new PollingRestServer(LOCALHOST, 8080, null);
    }

    @AfterEach
    void tearDown() {
        pollingRestServer.closeAllConnections();
    }

    @Test
    void startServer() {
        Assertions.assertDoesNotThrow(() -> pollingRestServer.startServer());
    }

    @Test
    void throwsIfPortInUseFromTcp() throws IOException {
        final PollingRestServer pollingRestServer = new PollingRestServer(LOCALHOST, 8080, null);
        pollingRestServer.start();
        Assertions.assertThrows(RestServerException.class, pollingRestServer::startServer);
    }
}