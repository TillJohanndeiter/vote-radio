package de.tjohanndeiter.exception.prototypes;

/**
 * Abstract class for all Exception on client side.
 */
public abstract class ClientException extends GSERadioException {

    private static final long serialVersionUID = 42L;

    public ClientException(final String message, final Exception e) {
        super(message, e);
    }
    public ClientException(final String message) {
        super(message);
    }
}
