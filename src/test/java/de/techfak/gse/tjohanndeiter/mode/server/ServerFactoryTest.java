package de.techfak.gse.tjohanndeiter.mode.server;

import de.techfak.gse.tjohanndeiter.GSERadio;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.mode.ProgramModeFactory;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.model.exception.shutdown.InvalidArgsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ServerFactoryTest {

    private final ServerFactory factory = new ServerFactory();


    //TODO: Find solution for ci tests with vlc
    @Test
    void createProgramMode() throws ShutdownException {
       ProgramMode programMode = factory.createProgramMode(GSERadio.SERVER_ARG, Thread.currentThread().getContextClassLoader().getResource("testMusicFiles").getPath());
        Assertions.assertTrue(programMode instanceof ServerMode);

    }


    @Test
    void serverAndClientInvalidCombinationTest() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(GSERadio.SERVER_ARG, GSERadio.CLIENT_ARG));
    }

    @Test
    void serverAndJukeBoxInvalidCombinationTest() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(GSERadio.SERVER_ARG, GSERadio.JUKEBOX_ARG));
    }

    @Test
    void serverAndJukeBoxAltInvalidCombinationTest() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(GSERadio.SERVER_ARG, GSERadio.ALT_JUKEBOX_ARG));
    }

    @Test
    void clientAndJukeBoxAltInvalidCombinationTest() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(GSERadio.CLIENT_ARG, GSERadio.ALT_JUKEBOX_ARG));
    }

    @Test
    void clientAndJukeBoxInvalidCombinationTest() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(GSERadio.CLIENT_ARG, GSERadio.JUKEBOX_ARG));
    }

    @Test
    void jukeBoxAndStreaming() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(GSERadio.JUKEBOX_ARG, "--streamPort=8080"));
    }

    @Test
    void jukeBoxAndPort() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(GSERadio.JUKEBOX_ARG, ProgramModeFactory.STREAMING_PORT_ARG + "8080"));
    }


    @Test
    void serverArgButWithoutServer() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(ProgramModeFactory.STREAMING_PORT_ARG + "8080", ProgramModeFactory.
                        STREAMING_PORT_ARG + "8080"));
    }
}