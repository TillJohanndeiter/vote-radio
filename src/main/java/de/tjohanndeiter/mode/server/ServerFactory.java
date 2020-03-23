package de.tjohanndeiter.mode.server;

import de.tjohanndeiter.controller.cmd.ServerController;
import de.tjohanndeiter.controller.cmd.TerminalController;
import de.tjohanndeiter.exception.prototypes.ShutdownException;
import de.tjohanndeiter.exception.shutdown.InvalidArgsException;
import de.tjohanndeiter.exception.shutdown.InvalidPortException;
import de.tjohanndeiter.exception.shutdown.MusicStreamPortInUseException;
import de.tjohanndeiter.mode.ProgramMode;
import de.tjohanndeiter.mode.ProgramModeFactory;
import de.tjohanndeiter.model.database.SongLibrary;
import de.tjohanndeiter.model.player.MusicPlayer;
import de.tjohanndeiter.model.player.OfflinePlayer;
import de.tjohanndeiter.model.player.StreamPlayer;
import de.tjohanndeiter.model.playlist.VoteList;
import de.tjohanndeiter.model.voting.ServerStrategy;
import de.tjohanndeiter.model.voting.VoteStrategy;

import java.util.Objects;

/**
 * Factory for {@link ServerMode}. Parse command lines for address and port of music stream and port of rest server.
 * If no filepath is available in #args the current path of program is selected as music folder.
 */
public class ServerFactory extends ProgramModeFactory {

    private static final int REPLAY_DEFAULT = 1;
    private static final int REST_PORT_DEFAULT = 8080;
    private static final int STREAM_PORT_DEFAULT = 1234;
    private static final String MULTICAST_DEFAULT = "239.255.0.1"; //NOPMD
    private static final String LOCAL_STREAM_ADDRESS = "none";

    @Override
    public ProgramMode createSpecificProgramMode(final String... args) throws ShutdownException {
        checkIfIllegalArgCombination(args);
        boolean streamPlay = false;
        String filepath = System.getProperty(CURRENT_DIR);
        String multicast = MULTICAST_DEFAULT;
        int streamPort = STREAM_PORT_DEFAULT;
        int restPort = REST_PORT_DEFAULT;
        int playsBeforeReplay = REPLAY_DEFAULT;
        SongLibrary songLibrary = null;


        for (int i = args.length - 1; i > 0; i--) {
            if (args[i].startsWith(STREAMING_PORT_ARG)) {
                streamPort = getArgumentAsInteger(args, i);
                streamPlay = true;
            } else if (args[i].startsWith(PORT_ARG)) {
                restPort = getArgumentAsInteger(args, i);
            } else if (args[i].startsWith(STREAMING_ADDRESS_ARG)) {
                multicast = getMulticast(args, i);
            } else if (args[i].startsWith(REPLAY_ARG)) {
                playsBeforeReplay = getReplay(args, i);
            } else {
                songLibrary = createSongLibrary(args, i);
            }
        }

        final VoteList voteList = new VoteList(Objects.requireNonNull(songLibrary), playsBeforeReplay);
        final VoteStrategy voteStrategy = new ServerStrategy(voteList);
        final StreamUrl streamUrl = createStreamUrl(streamPlay, multicast, streamPort);
        final UploadRequester uploadRequester = new UploadRequester(songLibrary, voteList);

        final MusicPlayer musicPlayer = createMusicPlayer(streamPlay, streamUrl, voteList, args);
        final UserManger userManger = new UserManger();
        final ModelConnector modelObserver = new ModelConnector(musicPlayer, userManger, streamUrl);
        final SessionHandler sessionHandler = new SessionHandler(voteStrategy, uploadRequester, modelObserver);


        final SocketRestServer socketRestServer = new SocketRestServer(restPort, sessionHandler, userManger);
        final TerminalController controller = new ServerController(socketRestServer);
        addObservers(voteList, musicPlayer, modelObserver, socketRestServer, controller);
        final Thread controllerThread = new Thread(controller::inputLoop);

        return new ServerMode(socketRestServer, musicPlayer, controllerThread);
    }

    private int getReplay(final String[] args, final int pos) throws InvalidArgsException {
        return getArgumentAsInteger(args, pos);
    }

    private StreamUrl createStreamUrl(final boolean streamPlay, final String multicast, final int streamPort) {
        if (streamPlay) {
            return new StreamUrl(multicast, streamPort);
        } else {
            return new StreamUrl(LOCAL_STREAM_ADDRESS, LOCAL_STREAM_ADDRESS);
        }
    }

    private void addObservers(final VoteList voteList, final MusicPlayer musicPlayer,
                              final ModelConnector modelObserver, final SocketRestServer socketRestServer,
                              final TerminalController controller) {
        voteList.addPropertyChangeListener(controller);
        voteList.addPropertyChangeListener(socketRestServer);
        voteList.addPropertyChangeListener(modelObserver);
        musicPlayer.addPropertyChangeListener(modelObserver);
        musicPlayer.addPropertyChangeListener(controller);
        musicPlayer.addPropertyChangeListener(socketRestServer);
    }

    private MusicPlayer createMusicPlayer(final boolean streamPlay, final StreamUrl streamUrl,
                                          final VoteList voteList, final String... args) throws
            InvalidPortException, MusicStreamPortInUseException, InvalidArgsException {

        final MusicPlayer musicPlayer;
        if (streamPlay) {
            try {
                musicPlayer = new StreamPlayer(voteList, streamUrl);
            } catch (NumberFormatException e) {
                throw new InvalidArgsException(e, args);
            }
        } else {
            musicPlayer = new OfflinePlayer(voteList);
        }
        return musicPlayer;
    }

    private String getMulticast(final String[] args, final int i) throws InvalidArgsException {
        try {
            return args[i].substring(args[i].indexOf('=') + 1);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new InvalidArgsException(e, args);
        }
    }

    private int getArgumentAsInteger(final String[] args, final int pos) throws InvalidArgsException {
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
