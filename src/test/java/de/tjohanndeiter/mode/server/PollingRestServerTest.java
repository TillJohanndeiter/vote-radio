package de.tjohanndeiter.mode.server;

import de.tjohanndeiter.exception.shutdown.RestServerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@SuppressWarnings("deprecation")
class PollingRestServerTest {

    private static final String LOCALHOST = "127.0.0.1";//NOPMD

    private PollingRestServer pollingRestServer = new PollingRestServer(LOCALHOST, 6060, null);

    @AfterEach
    void tearDown() {
        pollingRestServer.closeAllConnections();
        pollingRestServer.stop();
    }

    @Test
    void startServer() {
        Assertions.assertDoesNotThrow(() -> pollingRestServer.startServer());
    }

    @Test
    void throwsIfPortInUseFromTcp() throws IOException {
        pollingRestServer.start();
        final PollingRestServer anotherServer = new PollingRestServer(LOCALHOST, 6060, null);
        Assertions.assertThrows(RestServerException.class, anotherServer::startServer);
    }
}