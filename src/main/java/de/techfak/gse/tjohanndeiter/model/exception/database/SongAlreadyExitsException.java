package de.techfak.gse.tjohanndeiter.model.exception.database;

import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.GSERadioException;

public class SongAlreadyExitsException extends GSERadioException {

    private static final long serialVersionUID = 1L;

    public SongAlreadyExitsException(final Song song) {
        super("Song :" + song.toString() + "Already Exits");
    }
}
