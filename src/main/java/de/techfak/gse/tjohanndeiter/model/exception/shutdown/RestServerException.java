package de.techfak.gse.tjohanndeiter.model.exception.shutdown;

import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;

public class RestServerException extends ShutdownException {

    private static final long serialVersionUID = 42L;

    private static final int ERROR_CODE = 101;

    public RestServerException(final String message, final Exception e) {
        super(message, e, ERROR_CODE);
    }
}
