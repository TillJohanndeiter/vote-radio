package de.techfak.gse.tjohanndeiter.exception.shutdown;

import de.techfak.gse.tjohanndeiter.exception.prototypes.ParseException;

/**
 * Exception for an selected music folder without *.mp3 files.
 */
public class InvalidPathException extends ParseException {

    private static final int ERROR_CODE = 100;

    private static final long serialVersionUID = 1L;

    public InvalidPathException(final String message) {
        super(message, ERROR_CODE);
    }
}
