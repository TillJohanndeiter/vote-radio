package de.tjohanndeiter.mode.terminal;

import de.tjohanndeiter.exception.prototypes.ShutdownException;
import de.tjohanndeiter.mode.ProgramMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

class TerminalFactoryTest {

    private final TerminalFactory factory = new TerminalFactory();


    @Test
    void createProgramMode() throws ShutdownException {
        NativeDiscovery nativeDiscovery = new NativeDiscovery();
        if (nativeDiscovery.discover()) {
            ProgramMode programMode = factory.createProgramMode(Thread.currentThread().getContextClassLoader().getResource("testMusicFiles").getPath());
            Assertions.assertTrue(programMode instanceof TerminalMode);
        }
    }

}