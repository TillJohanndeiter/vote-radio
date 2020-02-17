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


    ServerMode(final String address, final int restPort, final MusicPlayer musicPlayer,
               final SessionHandler sessionHandler) {
        server = new SocketRestServer(address, restPort, sessionHandler);
        sessionHandler.getVoteList().addPropertyChangeListener(server);
        new Thread(() -> new ServerController(sessionHandler.getVoteList(), server).inputLoop()).start();
        musicPlayer.startPlay();
    }

    @Override
    public void startProgram() throws ShutdownException {
        server.startServer();
    }
}
