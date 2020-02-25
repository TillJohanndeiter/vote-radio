package de.techfak.gse.tjohanndeiter.mode;

import de.techfak.gse.tjohanndeiter.GSERadio;
import de.techfak.gse.tjohanndeiter.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.exception.shutdown.InvalidArgsException;

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

    public abstract ProgramMode createProgramMode(String... args) throws ShutdownException;

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
    protected String parseFilepath(final String[] args, final int posOfFilepath) {
        String filepath = System.getProperty(CURRENT_DIR);
        if (args.length > posOfFilepath && !args[posOfFilepath].startsWith(STREAMING_PORT_ARG)
                && !args[posOfFilepath].startsWith(PORT_ARG)) {
            filepath = args[posOfFilepath];
        }
        return filepath;
    }

    private boolean invalidArgCombination(final String... args) {
        final List<String> argList = Arrays.asList(args);
        final boolean containsServerArgs = argList.stream().anyMatch(s -> s.startsWith(STREAMING_PORT_ARG)
                || s.startsWith(PORT_ARG)
                || s.startsWith(STREAMING_ADDRESS_ARG));
        return argList.contains(GSERadio.SERVER_ARG) && argList.contains(GSERadio.CLIENT_ARG)
                || argList.contains(GSERadio.SERVER_ARG) && argList.contains(GSERadio.JUKEBOX_ARG)
                || argList.contains(GSERadio.SERVER_ARG) && argList.contains(GSERadio.ALT_JUKEBOX_ARG)
                || !argList.contains(GSERadio.SERVER_ARG) && containsServerArgs
                || argList.contains(GSERadio.CLIENT_ARG) && containsServerArgs
                || argList.contains(GSERadio.CLIENT_ARG) && argList.contains(GSERadio.ALT_JUKEBOX_ARG)
                || argList.contains(GSERadio.CLIENT_ARG) && argList.contains(GSERadio.JUKEBOX_ARG)
                || argList.contains(GSERadio.JUKEBOX_ARG) && containsServerArgs
                || argList.contains(GSERadio.ALT_JUKEBOX_ARG) && containsServerArgs;
    }
}
