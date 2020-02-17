package de.techfak.gse.tjohanndeiter.mode;

import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;

public interface ProgramMode {
    void startProgram() throws ShutdownException;
}
