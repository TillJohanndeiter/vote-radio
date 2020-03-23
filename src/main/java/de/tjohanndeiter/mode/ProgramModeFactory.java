package de.tjohanndeiter.mode;

import de.tjohanndeiter.VoteRadio;
import de.tjohanndeiter.exception.prototypes.ShutdownException;
import de.tjohanndeiter.exception.shutdown.InvalidArgsException;
import de.tjohanndeiter.model.database.SongLibrary;
import de.tjohanndeiter.model.database.SongLibraryFactory;
import de.tjohanndeiter.model.database.SongLibraryFactoryImpl;

import java.io.File;
import java.util.Arrays;
import java.util.List;


/**
 * Abstract factory for {@link ProgramMode}.
 */
public abstract class ProgramModeFactory {

    public static final String STREAMING_PORT_ARG = "--streamPort=";
    protected static final String CURRENT_DIR = "user.dir";
    protected static final String STREAMING_ADDRESS_ARG = "--streamAddress=";
    protected static final String PORT_ARG = "--restPort=";
    protected static final String REPLAY_ARG = "--replays=";

    public ProgramMode createProgramMode(String... args) throws ShutdownException {
        checkIfIllegalArgCombination(args);
        return createSpecificProgramMode(args);
    }

    public abstract ProgramMode createSpecificProgramMode(String... args) throws ShutdownException;

    /**
     * Checks if args combinations contains a illegal combination.
     * @param args args form cmd
     * @throws InvalidArgsException in case of an illegal combination
     */
    protected void checkIfIllegalArgCombination(final String... args) throws InvalidArgsException {
        if (invalidArgCombination(args)) {
            throw new InvalidArgsException(args);
        }
    }


    /**
     * Parse filepath at index #posOfFilepath from #args.
     * @param args cmd args
     * @param posOfFilepath index of filepath
     * @return parse filepath
     */
    protected SongLibrary createSongLibrary(final String[] args, final int posOfFilepath) throws ShutdownException {
        String filepath = System.getProperty(CURRENT_DIR);
        if (args.length > posOfFilepath && !args[posOfFilepath].startsWith(STREAMING_PORT_ARG)
                && !args[posOfFilepath].startsWith(PORT_ARG)) {
            filepath = args[posOfFilepath];
        }
        final SongLibraryFactory songLibraryFactory = new SongLibraryFactoryImpl();

        return songLibraryFactory.createSongLibrary(new File(filepath));
    }

    private boolean invalidArgCombination(final String... args) {
        final List<String> argList = Arrays.asList(args);
        final boolean containsServerArgs = argList.stream().anyMatch(s -> s.startsWith(STREAMING_PORT_ARG)
                || s.startsWith(PORT_ARG)
                || s.startsWith(STREAMING_ADDRESS_ARG));
        return argList.contains(VoteRadio.SERVER_ARG) && argList.contains(VoteRadio.CLIENT_ARG)
                || argList.contains(VoteRadio.SERVER_ARG) && argList.contains(VoteRadio.JUKEBOX_ARG)
                || argList.contains(VoteRadio.SERVER_ARG) && argList.contains(VoteRadio.ALT_JUKEBOX_ARG)
                || !argList.contains(VoteRadio.SERVER_ARG) && containsServerArgs
                || argList.contains(VoteRadio.CLIENT_ARG) && containsServerArgs
                || argList.contains(VoteRadio.CLIENT_ARG) && argList.contains(VoteRadio.ALT_JUKEBOX_ARG)
                || argList.contains(VoteRadio.CLIENT_ARG) && argList.contains(VoteRadio.JUKEBOX_ARG)
                || argList.contains(VoteRadio.JUKEBOX_ARG) && containsServerArgs
                || argList.contains(VoteRadio.ALT_JUKEBOX_ARG) && containsServerArgs;
    }
}
