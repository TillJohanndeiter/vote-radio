package de.techfak.gse.tjohanndeiter.mode.server;

import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.server.RestServer;

/**
 * Mode for server.
 */
public class ServerMode implements ProgramMode {

    private RestServer restServer;
    private MusicPlayer musicPlayer;
    private Thread controllerThread;


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
