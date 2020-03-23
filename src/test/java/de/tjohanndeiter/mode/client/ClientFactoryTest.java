package de.tjohanndeiter.mode.client;

import de.tjohanndeiter.VoteRadio;
import de.tjohanndeiter.mode.ProgramMode;
import de.tjohanndeiter.exception.prototypes.ShutdownException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

public class ClientFactoryTest {

    private final ClientFactory factory = new ClientFactory();

    private NativeDiscovery nativeDiscovery = new NativeDiscovery();


    @Test
    void createProgramModeTest() throws ShutdownException {
        if (nativeDiscovery.discover()) {
            final ProgramMode programMode = factory.createProgramMode(VoteRadio.CLIENT_ARG);
            Assertions.assertTrue(programMode instanceof ClientMode);
        }
    }
}