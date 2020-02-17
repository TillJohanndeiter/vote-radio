package de.techfak.gse.tjohanndeiter.mode.jukebox;

import de.techfak.gse.tjohanndeiter.GSERadio;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.mode.ProgramModeFactory;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JukeBoxFactoryTest {

    private ProgramModeFactory factory = new JukeBoxFactory();

    @Test
    void createProgramMode() throws ShutdownException {
        final ProgramMode programMode = factory.createProgramMode(GSERadio.JUKEBOX_ARG);
        Assertions.assertTrue(programMode instanceof JukeBoxMode);
    }

    @Test
    void jukeBoxModeAlternative() throws ShutdownException {
        final ProgramMode programMode = factory.createProgramMode(GSERadio.ALT_JUKEBOX_ARG);
        Assertions.assertTrue(programMode instanceof JukeBoxMode);
    }
}