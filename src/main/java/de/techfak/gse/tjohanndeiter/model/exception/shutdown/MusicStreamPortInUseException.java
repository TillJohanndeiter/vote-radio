package de.techfak.gse.tjohanndeiter.model.exception.shutdown;

import de.techfak.gse.tjohanndeiter.model.exception.prototypes.StartPlayerException;

/**
 * Throw in case of a music port that is already in use.
 */
public class MusicStreamPortInUseException extends StartPlayerException {

    private static final long serialVersionUID = 1L;

    public MusicStreamPortInUseException(final String adress, final int port) {
        super("Music stream failed. Maybe adress: " + adress + " Port: " + port + " is used already. ");
    }
}
