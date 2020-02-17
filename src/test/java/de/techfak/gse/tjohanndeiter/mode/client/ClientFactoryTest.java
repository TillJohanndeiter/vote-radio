package de.techfak.gse.tjohanndeiter.mode.client;

import de.techfak.gse.tjohanndeiter.GSERadio;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClientFactoryTest {

    private ClientFactory factory = new ClientFactory();

    @Test
    void createProgramMode() throws ShutdownException {
        final ProgramMode programMode = factory.createProgramMode(GSERadio.CLIENT_ARG);
        Assertions.assertTrue(programMode instanceof ClientMode);
    }
}