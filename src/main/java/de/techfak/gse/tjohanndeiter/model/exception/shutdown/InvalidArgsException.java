package de.techfak.gse.tjohanndeiter.model.exception.shutdown;


import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;

import java.util.Arrays;

/**
 * Throw in case of invalid command line arguments.
 */
public class InvalidArgsException extends ShutdownException {

    private static final long serialVersionUID = 1L;

    private static final int ERROR_CODE = 103;
    private static final String IS_NOT_VALID = " is not valid";
    private static final String THE_ARGUMENT_COMBINATION = "The argument combination: ";

    public InvalidArgsException(final Exception e, final String... args) {
        super(THE_ARGUMENT_COMBINATION + Arrays.toString(args) + IS_NOT_VALID, e, ERROR_CODE);
    }

    public InvalidArgsException(final String... args) {
        super(THE_ARGUMENT_COMBINATION + Arrays.toString(args) + IS_NOT_VALID, ERROR_CODE);
    }
}
