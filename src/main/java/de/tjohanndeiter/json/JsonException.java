package de.tjohanndeiter.json;

import de.tjohanndeiter.exception.prototypes.GSERadioException;

public class JsonException extends GSERadioException {

    private static final long serialVersionUID = 1L;

    JsonException(final String message, final Exception e) {
        super(message, e);
    }
}
