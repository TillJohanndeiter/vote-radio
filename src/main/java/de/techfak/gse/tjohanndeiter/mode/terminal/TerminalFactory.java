package de.techfak.gse.tjohanndeiter.mode.terminal;

import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.mode.ProgramModeFactory;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;
import de.techfak.gse.tjohanndeiter.model.database.SongLibraryFactory;
import de.techfak.gse.tjohanndeiter.model.database.SongLibraryVlcJFactory;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;

import java.io.File;

public class TerminalFactory extends ProgramModeFactory {

    private SongLibraryFactory factory = new SongLibraryVlcJFactory();

    @Override
    public ProgramMode createProgramMode(final String... args) throws ShutdownException {
        checkIfIllegalArgCombination(args);
        final String filepath = parseFilepath(args, 0);
        final SongLibrary songLibrary = factory.createSongLibrary(new File(filepath));
        return new TerminalMode(songLibrary);
    }
}
