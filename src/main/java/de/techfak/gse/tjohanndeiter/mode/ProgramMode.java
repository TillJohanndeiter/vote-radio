package de.techfak.gse.tjohanndeiter.mode;

import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;

/**
 * Interface for mode in which you can start the program. Only method does start the mode. Currently implemented modi
 * are terminal random play, offline vote jukebox, client and server mode with votes.
 */
public interface ProgramMode {
    void startProgram() throws ShutdownException;
}
