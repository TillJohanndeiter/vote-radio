package de.techfak.gse.tjohanndeiter.exception.prototypes;

/**
 * Own Exceptions for the project.
 */
public abstract class GSERadioException extends Exception {

    private static final long serialVersionUID = 1L;

    public GSERadioException(final String message) {
        super(message);
    }

    public GSERadioException(final String message, final Exception e) {
        super(message, e);
    }
}
