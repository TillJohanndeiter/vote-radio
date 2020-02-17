package de.techfak.gse.tjohanndeiter.model.exception.database;

import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ClientException;

public class CurrentSongRequestException extends ClientException {

    private static final long serialVersionUID = 42L;

    public CurrentSongRequestException(final String message, final Exception e) {
        super(message, e);
    }

    public CurrentSongRequestException(final String message) {
        super(message);
    }
}
