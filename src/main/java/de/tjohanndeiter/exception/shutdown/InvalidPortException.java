package de.tjohanndeiter.exception.shutdown;

import de.tjohanndeiter.exception.prototypes.StartPlayerException;

/**
 * Throw in case of invalid port argument.
 */
public class InvalidPortException extends StartPlayerException {

    private static final long serialVersionUID = 42L;

    public InvalidPortException(final int port, final Exception e) {
        super("Port :" + port + " is not integer between 0 and 65535", e);
    }
}
