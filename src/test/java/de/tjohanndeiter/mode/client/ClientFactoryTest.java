package de.tjohanndeiter.mode.client;

import de.tjohanndeiter.VoteRadio;
import de.tjohanndeiter.exception.prototypes.ShutdownException;
import de.tjohanndeiter.mode.ProgramMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClientFactoryTest {

    private final ClientFactory factory = new ClientFactory();

    @Test
    void createProgramModeTest() throws ShutdownException {
        final ProgramMode programMode = factory.createProgramMode(VoteRadio.CLIENT_ARG);
        Assertions.assertTrue(programMode instanceof ClientMode);

    }
}