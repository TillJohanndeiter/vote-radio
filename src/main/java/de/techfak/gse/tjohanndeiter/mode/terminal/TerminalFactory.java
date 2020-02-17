package de.techfak.gse.tjohanndeiter.mode.terminal;

import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.mode.ProgramModeFactory;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;

public class TerminalFactory extends ProgramModeFactory {

    @Override
    public ProgramMode createProgramMode(final String... args) throws ShutdownException {
        checkIfIllegalArgCombination(args);
        final String filepath = parseFilepath(args, 0);
        return new TerminalMode(filepath);
    }
}
