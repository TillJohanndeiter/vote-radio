package de.tjohanndeiter.mode.server;

import de.tjohanndeiter.mode.ProgramMode;
import de.tjohanndeiter.exception.prototypes.ShutdownException;
import de.tjohanndeiter.model.player.MusicPlayer;


/**
 * Program mode as rest server and additional music stream. Also contains a simple controller.
 */
public class ServerMode implements ProgramMode {

    private final RestServer restServer;
    private final MusicPlayer musicPlayer;
    private final Thread controllerThread;


    ServerMode(final RestServer restServer, final MusicPlayer musicPlayer, final Thread controllerThread) {
        this.musicPlayer = musicPlayer;
        this.restServer = restServer;
        this.controllerThread = controllerThread;
    }

    @Override
    public void startProgram() throws ShutdownException {
        restServer.startServer();
        musicPlayer.startPlay();
        controllerThread.start();
    }
}
