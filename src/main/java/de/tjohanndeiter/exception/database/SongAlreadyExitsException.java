package de.tjohanndeiter.exception.database;

import de.tjohanndeiter.exception.prototypes.GSERadioException;
import de.tjohanndeiter.model.database.Song;

/**
 * Throw in case of song upload that already exits.
 */
public class SongAlreadyExitsException extends GSERadioException {

    private static final long serialVersionUID = 1L;

    public SongAlreadyExitsException(final Song song) {
        super("Song :" + song.toString() + "Already Exits");
    }
}
