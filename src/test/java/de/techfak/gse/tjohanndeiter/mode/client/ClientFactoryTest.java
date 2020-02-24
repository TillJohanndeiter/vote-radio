package de.techfak.gse.tjohanndeiter.mode.client;

import de.techfak.gse.tjohanndeiter.GSERadio;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

public class ClientFactoryTest {

    private final ClientFactory factory = new ClientFactory();

    private NativeDiscovery nativeDiscovery = new NativeDiscovery();


    @Test
    void createProgramModeTest() throws ShutdownException {
        if (nativeDiscovery.discover()) {
            final ProgramMode programMode = factory.createProgramMode(GSERadio.CLIENT_ARG);
            Assertions.assertTrue(programMode instanceof ClientMode);
        }
    }
}