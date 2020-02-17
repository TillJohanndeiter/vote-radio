package de.techfak.gse.tjohanndeiter.model.exception.shutdown;

import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;

public class VlcJException extends ShutdownException {

    private static final long serialVersionUID = 42L;
    private static final int ERROR_CODE = 105;

    public VlcJException(final String message, final Exception e) {
        super(message, e, ERROR_CODE);
    }
}
