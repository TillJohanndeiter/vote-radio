package de.techfak.gse.tjohanndeiter.exception.shutdown;

import de.techfak.gse.tjohanndeiter.exception.prototypes.ShutdownException;

/**
 * Exception if any kind of VLCJ exceptions thrown.
 */
public class VlcJException extends ShutdownException {

    private static final long serialVersionUID = 42L;
    private static final int ERROR_CODE = 105;

    public VlcJException(final String message, final Exception e) {
        super(message, e, ERROR_CODE);
    }
}
