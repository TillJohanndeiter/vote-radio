package de.tjohanndeiter.mode.terminal;

import de.tjohanndeiter.controller.cmd.TerminalController;
import de.tjohanndeiter.mode.ProgramMode;
import de.tjohanndeiter.model.player.MusicPlayer;

/**
 * Commandline terminal. Handles user interaction with the model.
 */
public class TerminalMode implements ProgramMode {


    private final TerminalController terminalController;
    private final MusicPlayer musicPlayer;


    public TerminalMode(final MusicPlayer musicPlayer, final TerminalController terminalController) {
        this.musicPlayer = musicPlayer;
        this.terminalController = terminalController;
    }

    @Override
    public void startProgram() {
        musicPlayer.startPlay();
        terminalController.inputLoop();
    }
}
