package de.tjohanndeiter.mode.client;

import de.tjohanndeiter.mode.ProgramMode;
import de.tjohanndeiter.mode.ProgramModeFactory;


/**
 * Factory for {@link ClientMode}.
 */
public class ClientFactory extends ProgramModeFactory {

    /**
     * Checks if #args are contradictory and if not creates {@link ClientMode}.
     * @param args Cmd args
     * @return created Client Mode
     */
    @Override
    public ProgramMode createSpecificProgramMode(final String... args) {
        return new ClientMode();
    }
}
