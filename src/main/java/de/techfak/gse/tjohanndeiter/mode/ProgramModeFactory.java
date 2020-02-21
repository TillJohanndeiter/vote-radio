package de.techfak.gse.tjohanndeiter.mode;

import de.techfak.gse.tjohanndeiter.GSERadio;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.model.exception.shutdown.InvalidArgsException;

import java.util.Arrays;
import java.util.List;

public abstract class ProgramModeFactory {

    public static final String STREAMING_PORT_ARG = "--streamPort=";
    protected static final String CURRENT_DIR = "user.dir";
    protected static final String STREAMING_ADDRESS_ARG = "--streamAddress=";
    protected static final String PORT_ARG = "--restPort=";

    public abstract ProgramMode createProgramMode(String... args) throws ShutdownException;

    protected void checkIfIllegalArgCombination(final String... args) throws InvalidArgsException {
        if (invalidArgCombination(args)) {
            throw new InvalidArgsException(args);
        }
    }


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
