package de.techfak.gse.tjohanndeiter.model.database;

import de.techfak.gse.tjohanndeiter.exception.prototypes.ShutdownException;

import java.io.File;

/**
 * Parse a folder an creates with the mp3 files the {@link SongLibrary}.
 */
public interface SongLibraryFactory {

    SongLibrary createSongLibrary(final File file) throws ShutdownException;
}
