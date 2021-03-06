package de.tjohanndeiter.mode;

import de.tjohanndeiter.exception.prototypes.ShutdownException;

/**
 * Interface for mode in which you can start the program. Only method does start the mode. Currently implemented modi
 * are terminal random play, offline vote jukebox, client and server mode with votes.
 */
public interface ProgramMode {
    void startProgram() throws ShutdownException;
}
