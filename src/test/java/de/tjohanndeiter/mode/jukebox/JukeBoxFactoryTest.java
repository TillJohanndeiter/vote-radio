package de.tjohanndeiter.mode.jukebox;

import de.tjohanndeiter.VoteRadio;
import de.tjohanndeiter.exception.prototypes.ShutdownException;
import de.tjohanndeiter.mode.ProgramMode;
import de.tjohanndeiter.mode.ProgramModeFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class JukeBoxFactoryTest {

    private final ProgramModeFactory factory = new JukeBoxFactory();


    @Test
    void createProgramMode() throws ShutdownException {
        final ProgramMode programMode = factory.createProgramMode(VoteRadio.JUKEBOX_ARG,
                Objects.requireNonNull(
                        Thread.currentThread().getContextClassLoader().getResource("testMusicFiles")).getPath());
        Assertions.assertTrue(programMode instanceof JukeBoxMode);
    }


    @Test
    void jukeBoxModeAlternative() throws ShutdownException {
        final ProgramMode programMode = factory.createProgramMode(VoteRadio.ALT_JUKEBOX_ARG, Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource("testMusicFiles")).getPath());
        Assertions.assertTrue(programMode instanceof JukeBoxMode);

    }
}