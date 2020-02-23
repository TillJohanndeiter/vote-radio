package de.techfak.gse.tjohanndeiter.controller;

final class ControllerUtils {

    private static final String ZERO = "0";
    private static final int MIL_TO_SECONDS = 1000;
    private static final int MINUTE = 60;
    private static final int RANGE = 10;

    private ControllerUtils() {

    }


    static String generateTimeFormat(long lengthInMil) {
        String result = null;
        long length = lengthInMil / MIL_TO_SECONDS;
        if (length / MINUTE < RANGE) {
            result = ZERO;
        }
        result = result + length / MINUTE + ':';
        if (length % MINUTE < RANGE) {
            result = result + ZERO;
        }
        return result + length % MINUTE;
    }

}
