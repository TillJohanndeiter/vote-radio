package de.techfak.gse.tjohanndeiter.model.exception.shutdown;

import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ParseException;

/**
 * Exception for an selected music folder without *.mp3 files.
 */
public class NoMp3FilesException extends ParseException {

    private static final int ERROR_CODE = 100;

    private static final long serialVersionUID = 1L;

    public NoMp3FilesException(final String message) {
        super(message, ERROR_CODE);
    }
}
