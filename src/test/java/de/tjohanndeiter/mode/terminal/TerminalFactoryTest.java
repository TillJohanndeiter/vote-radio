package de.tjohanndeiter.mode.terminal;

import de.tjohanndeiter.exception.prototypes.ShutdownException;
import de.tjohanndeiter.mode.ProgramMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class TerminalFactoryTest {

    private final TerminalFactory factory = new TerminalFactory();


    @Test
    void createProgramMode() throws ShutdownException {
        ProgramMode programMode = factory.createProgramMode(Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource("testMusicFiles")).getPath());
        Assertions.assertTrue(programMode instanceof TerminalMode);
    }

}