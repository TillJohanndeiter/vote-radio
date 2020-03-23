package de.techfak.gse.tjohanndeiter.mode.client;

import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.mode.ProgramModeFactory;
import de.techfak.gse.tjohanndeiter.exception.prototypes.ShutdownException;


/**
 * Factory for {@link ClientMode}.
 */
public class ClientFactory extends ProgramModeFactory {

    /**
     * Checks if #args are contradictory and if not creates {@link ClientMode}.
     * @param args Cmd args
     * @return created Client Mode
     * @throws ShutdownException in case of contradictory arguments
     */
    @Override
    public ProgramMode createSpecificProgramMode(final String... args) throws ShutdownException {
        return new ClientMode();
    }
}
