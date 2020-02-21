package de.techfak.gse.tjohanndeiter.mode.server;

import de.techfak.gse.tjohanndeiter.controller.cmd.ServerController;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.mode.ProgramModeFactory;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;
import de.techfak.gse.tjohanndeiter.model.database.SongLibraryVlcJFactory;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.model.exception.shutdown.InvalidArgsException;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.player.OfflinePlayer;
import de.techfak.gse.tjohanndeiter.model.player.StreamPlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.server.ModelConnector;
import de.techfak.gse.tjohanndeiter.model.server.SessionHandler;
import de.techfak.gse.tjohanndeiter.model.server.SocketRestServer;
import de.techfak.gse.tjohanndeiter.model.server.StreamUrl;
import de.techfak.gse.tjohanndeiter.model.server.UploadRequester;
import de.techfak.gse.tjohanndeiter.model.voting.JukeBoxStrategy;
import de.techfak.gse.tjohanndeiter.model.voting.VoteStrategy;

import java.io.File;

public class ServerFactory extends ProgramModeFactory {

    private static final int REST_PORT_DEFAULT = 8080;
    private static final int STREAM_PORT_DEFAULT = 1234;
    private static final String MULTICAST_DEFAULT = "239.255.0.1"; //NOPMD
    private static final String LOCALHOST = "127.0.0.1"; //NOPMD


    @Override
    public ProgramMode createProgramMode(final String... args) throws ShutdownException {
        checkIfIllegalArgCombination(args);
        final ProgramMode programMode;
        boolean streamPlay = false;
        String filepath = System.getProperty(CURRENT_DIR);
        String multicast = MULTICAST_DEFAULT;
        int streamPort = STREAM_PORT_DEFAULT;
        int restPort = REST_PORT_DEFAULT;


        for (int i = args.length - 1; i > 0; i--) {
            if (args[i].startsWith(STREAMING_PORT_ARG)) {
                streamPort = getStreamPort(i, args);
                streamPlay = true;
            } else if (args[i].startsWith(PORT_ARG)) {
                restPort = getRestPort(args, i);
            } else if (args[i].startsWith(STREAMING_ADDRESS_ARG)) {
                multicast = getMulticast(args, i);
            } else {
                filepath = parseFilepath(args, i);
            }
        }

        final SongLibrary songLibrary = new SongLibraryVlcJFactory().createSongLibrary(new File(filepath));
        final VoteList voteList = new VoteList(songLibrary, 0);

        MusicPlayer musicPlayer;
        if (streamPlay) {
            try {
                musicPlayer = new StreamPlayer(voteList, multicast, streamPort);
            } catch (NumberFormatException e) {
                throw new InvalidArgsException(e, args);
            }
        } else {
            musicPlayer = new OfflinePlayer(voteList);
        }



        final StreamUrl streamLocation = new StreamUrl(multicast, streamPort);
        final VoteStrategy voteStrategy = new JukeBoxStrategy(voteList);
        final UploadRequester uploadRequester = new UploadRequester(songLibrary, voteList);
        final ModelConnector modelObserver = new ModelConnector(musicPlayer);
        voteList.addPropertyChangeListener(modelObserver);
        final SessionHandler sessionHandler = new SessionHandler(streamLocation, voteStrategy,
                uploadRequester, modelObserver);
        final SocketRestServer socketRestServer = new SocketRestServer(LOCALHOST, restPort, sessionHandler);
        final ServerController controller = new ServerController(voteList, socketRestServer);
        final Thread controllerThread = new Thread(controller::inputLoop);

        programMode = new ServerMode(socketRestServer, musicPlayer, controllerThread);
        return programMode;
    }


    private int getStreamPort(final int i, final String... args) throws InvalidArgsException {
        try {
            return Integer.parseInt(args[i].substring(args[i].indexOf('=') + 1));
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new InvalidArgsException(e, args);
        }
    }

    private String getMulticast(final String[] args, final int i) throws InvalidArgsException {
        try {
            return args[i].substring(args[i].indexOf('=') + 1);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new InvalidArgsException(e, args);
        }
    }


    private int getRestPort(final String[] args, final int pos) throws InvalidArgsException {

        int port;
        try {
            final int startOfPortNumber = args[pos].indexOf('=') + 1;
            port = Integer.parseInt(args[pos].substring(startOfPortNumber));
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new InvalidArgsException(e, args);
        }
        return port;
    }

}
