package de.techfak.gse.tjohanndeiter.mode.server;

import de.techfak.gse.tjohanndeiter.controller.cmd.ServerController;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.server.SessionHandler;
import de.techfak.gse.tjohanndeiter.model.server.SocketRestServer;

/**
 * Mode for server.
 */
public class ServerMode implements ProgramMode {

    private SocketRestServer server;
    private MusicPlayer musicPlayer;


    ServerMode(final String address, final int restPort, final MusicPlayer musicPlayer,
               final SessionHandler sessionHandler) {
        this.musicPlayer = musicPlayer;
        server = new SocketRestServer(address, restPort, sessionHandler);
        sessionHandler.getVoteList().addPropertyChangeListener(server);
        new Thread(() -> new ServerController(sessionHandler.getVoteList(), server).inputLoop()).start();
    }

    @Override
    public void startProgram() throws ShutdownException {
        server.startServer();
        musicPlayer.startPlay();
    }
}
