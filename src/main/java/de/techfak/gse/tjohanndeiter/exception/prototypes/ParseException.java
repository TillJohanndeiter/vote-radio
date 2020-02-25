package de.techfak.gse.tjohanndeiter.exception.prototypes;

/**
 * Abstract class for errors in the parsing process.
 */
public abstract class ParseException extends ShutdownException {

    private static final long serialVersionUID = 1L;

    public ParseException(final String message, final int errorCode) {
        super(message, errorCode);
    }


}
