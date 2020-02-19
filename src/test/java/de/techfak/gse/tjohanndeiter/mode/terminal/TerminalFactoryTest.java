package de.techfak.gse.tjohanndeiter.mode.terminal;

import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TerminalFactoryTest {

    private TerminalFactory factory = new TerminalFactory();

    @Test
    void createProgramMode() throws ShutdownException {
        ProgramMode programMode = factory.createProgramMode(Thread.currentThread().getContextClassLoader().getResource("testMusicFiles").getPath());
        Assertions.assertTrue(programMode instanceof TerminalMode);
    }

}