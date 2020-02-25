package de.techfak.gse.tjohanndeiter.mode.jukebox;

import de.techfak.gse.tjohanndeiter.GSERadio;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.mode.ProgramModeFactory;
import de.techfak.gse.tjohanndeiter.exception.prototypes.ShutdownException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

class JukeBoxFactoryTest {

    private final ProgramModeFactory factory = new JukeBoxFactory();
    private NativeDiscovery nativeDiscovery = new NativeDiscovery();



    @Test
    void createProgramMode() throws ShutdownException {
        if (nativeDiscovery.discover()) {
            final ProgramMode programMode = factory.createProgramMode(GSERadio.JUKEBOX_ARG,
                    Thread.currentThread().getContextClassLoader().getResource("testMusicFiles").getPath());
            Assertions.assertTrue(programMode instanceof JukeBoxMode);
        }
    }

    @Test
    void jukeBoxModeAlternative() throws ShutdownException {
        if (nativeDiscovery.discover()) {
            final ProgramMode programMode = factory.createProgramMode(GSERadio.ALT_JUKEBOX_ARG, Thread.currentThread().getContextClassLoader().getResource("testMusicFiles").getPath());
            Assertions.assertTrue(programMode instanceof JukeBoxMode);
        }
    }
}