package de.techfak.gse.tjohanndeiter.mode.jukebox;

import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.mode.ProgramModeFactory;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;

public class JukeBoxFactory extends ProgramModeFactory {

    @Override
    public ProgramMode createProgramMode(final String... args) throws ShutdownException {
        checkIfIllegalArgCombination(args);
        final String filepath = parseFilepath(args, 1);
        return new JukeBoxMode(filepath);
    }
}
