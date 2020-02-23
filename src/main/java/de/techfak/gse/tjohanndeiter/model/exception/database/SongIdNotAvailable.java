package de.techfak.gse.tjohanndeiter.model.exception.database;

import de.techfak.gse.tjohanndeiter.model.exception.prototypes.GSERadioException;

/**
 * Throw in case of any kind of request or vote for a id that doesn't belong to any song.
 */
public class SongIdNotAvailable extends GSERadioException {

    private static final long serialVersionUID = 1L;

    public SongIdNotAvailable(final int message) {
        super("Id: " + message + " not available");
    }
}
