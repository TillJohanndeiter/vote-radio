package de.techfak.gse.tjohanndeiter.mode.terminal;

import de.techfak.gse.tjohanndeiter.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
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