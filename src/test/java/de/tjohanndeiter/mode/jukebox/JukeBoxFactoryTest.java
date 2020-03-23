package de.tjohanndeiter.mode.jukebox;

import de.tjohanndeiter.VoteRadio;
import de.tjohanndeiter.mode.ProgramMode;
import de.tjohanndeiter.mode.ProgramModeFactory;
import de.tjohanndeiter.exception.prototypes.ShutdownException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

class JukeBoxFactoryTest {

    private final ProgramModeFactory factory = new JukeBoxFactory();
    private NativeDiscovery nativeDiscovery = new NativeDiscovery();



    @Test
    void createProgramMode() throws ShutdownException {
        if (nativeDiscovery.discover()) {
            final ProgramMode programMode = factory.createProgramMode(VoteRadio.JUKEBOX_ARG,
                    Thread.currentThread().getContextClassLoader().getResource("testMusicFiles").getPath());
            Assertions.assertTrue(programMode instanceof JukeBoxMode);
        }
    }

    @Test
    void jukeBoxModeAlternative() throws ShutdownException {
        if (nativeDiscovery.discover()) {
            final ProgramMode programMode = factory.createProgramMode(VoteRadio.ALT_JUKEBOX_ARG, Thread.currentThread().getContextClassLoader().getResource("testMusicFiles").getPath());
            Assertions.assertTrue(programMode instanceof JukeBoxMode);
        }
    }
}