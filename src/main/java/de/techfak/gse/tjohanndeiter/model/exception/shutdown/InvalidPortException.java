package de.techfak.gse.tjohanndeiter.model.exception.shutdown;

import de.techfak.gse.tjohanndeiter.model.exception.prototypes.StartPlayerException;

public class InvalidPortException extends StartPlayerException {

    private static final long serialVersionUID = 42L;

    public InvalidPortException(final int port, final Exception e) {
        super("Port :" + port + " is not between 0 and 6", e);
    }
}
