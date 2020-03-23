package de.tjohanndeiter.exception.prototypes;



/**
 * Throw in case of {@link de.tjohanndeiter.model.player.MusicPlayer} cannot start to play.
 */
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
