package de.tjohanndeiter;

import de.tjohanndeiter.mode.ProgramMode;
import de.tjohanndeiter.mode.ProgramModeFactory;
import de.tjohanndeiter.mode.client.ClientFactory;
import de.tjohanndeiter.mode.jukebox.JukeBoxFactory;
import de.tjohanndeiter.mode.server.ServerFactory;
import de.tjohanndeiter.mode.terminal.TerminalFactory;
import de.tjohanndeiter.exception.prototypes.ShutdownException;
import org.apache.log4j.BasicConfigurator;


/**
 * Main class.
 */
public final class VoteRadio {


    public static final String SERVER_ARG = "--server";
    public static final String CLIENT_ARG = "--client";
    public static final String JUKEBOX_ARG = "--jukebox";
    public static final String ALT_JUKEBOX_ARG = "-g";

    private VoteRadio() {
    }

    /**
     * Main method of project. Start GSERadio in {@link ProgramMode} depend on commandline #args.
     *
     * @param args commandline args
     */
    public static void main(final String... args) {

        try {
            initLibraries();
            ProgramModeFactory factory = new TerminalFactory();

            if (args.length > 0 && args[0].equals(CLIENT_ARG)) {
                factory = new ClientFactory();
            } else if (args.length > 0 && (args[0].equals(JUKEBOX_ARG) || args[0].equals(ALT_JUKEBOX_ARG))) {
                factory = new JukeBoxFactory();
            } else if (args.length > 0 && args[0].equals(SERVER_ARG)) {
                factory = new ServerFactory();
            }

            final ProgramMode programMode = factory.createProgramMode(args);
            programMode.startProgram();
        } catch (final ShutdownException e) {
            System.out.println("ERROR :" + e.getMessage()); //NOPMD
            e.printStackTrace(); //NOPMD
            System.exit(e.getErrorCode()); //NOPMD
        }
    }

    private static void initLibraries() {
        BasicConfigurator.configure();
    }

}
