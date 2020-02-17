package de.techfak.gse.tjohanndeiter.model.exception.prototypes;

public abstract class StartPlayerException extends ShutdownException {

    private static final long serialVersionUID = 1L;
    private static final int ERROR_CODE = 102;

    public StartPlayerException(final String message, final Exception e) {
        super(message, e, ERROR_CODE);
    }

    public StartPlayerException(final String message) {
        super(message, ERROR_CODE);
    }
}
