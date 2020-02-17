package de.techfak.gse.tjohanndeiter.model.exception.prototypes;

/**
 * Extends {@linkplain GSERadioException} with a {{@link #errorCode}}. Used in case of not treatable errors which
 * lead to a shutdown
 */
public abstract class ShutdownException extends GSERadioException {

    private static final long serialVersionUID = 1L;

    private final int errorCode;

    public ShutdownException(final String message, final Exception e, final int errorCode) {
        super(message, e);
        this.errorCode = errorCode;
    }

    public ShutdownException(final String message, final int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }


    public int getErrorCode() {
        return errorCode;
    }


}
