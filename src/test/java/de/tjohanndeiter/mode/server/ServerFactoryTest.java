package de.tjohanndeiter.mode.server;

import de.tjohanndeiter.VoteRadio;
import de.tjohanndeiter.exception.prototypes.ShutdownException;
import de.tjohanndeiter.exception.shutdown.InvalidArgsException;
import de.tjohanndeiter.mode.ProgramMode;
import de.tjohanndeiter.mode.ProgramModeFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class ServerFactoryTest {

    private final ServerFactory factory = new ServerFactory();


    @Test
    void createProgramMode() throws ShutdownException {

        ProgramMode programMode = factory.createProgramMode(VoteRadio.SERVER_ARG, Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource("testMusicFiles")).getPath());
        Assertions.assertTrue(programMode instanceof ServerMode);

    }


    @Test
    void serverAndClientInvalidCombinationTest() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(VoteRadio.SERVER_ARG, VoteRadio.CLIENT_ARG));
    }

    @Test
    void serverAndJukeBoxInvalidCombinationTest() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(VoteRadio.SERVER_ARG, VoteRadio.JUKEBOX_ARG));
    }

    @Test
    void serverAndJukeBoxAltInvalidCombinationTest() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(VoteRadio.SERVER_ARG, VoteRadio.ALT_JUKEBOX_ARG));
    }

    @Test
    void clientAndJukeBoxAltInvalidCombinationTest() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(VoteRadio.CLIENT_ARG, VoteRadio.ALT_JUKEBOX_ARG));
    }

    @Test
    void clientAndJukeBoxInvalidCombinationTest() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(VoteRadio.CLIENT_ARG, VoteRadio.JUKEBOX_ARG));
    }

    @Test
    void jukeBoxAndStreaming() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(VoteRadio.JUKEBOX_ARG, "--streamPort=8080"));
    }

    @Test
    void jukeBoxAndPort() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(VoteRadio.JUKEBOX_ARG, ProgramModeFactory.STREAMING_PORT_ARG + "8080"));
    }


    @Test
    void serverArgButWithoutServer() {
        Assertions.assertThrows(InvalidArgsException.class, () ->
                factory.createProgramMode(ProgramModeFactory.STREAMING_PORT_ARG + "8080", ProgramModeFactory.
                        STREAMING_PORT_ARG + "8080"));
    }
}