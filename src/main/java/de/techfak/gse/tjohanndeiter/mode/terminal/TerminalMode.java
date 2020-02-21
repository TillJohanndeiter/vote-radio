package de.techfak.gse.tjohanndeiter.mode.terminal;

import de.techfak.gse.tjohanndeiter.controller.cmd.TerminalController;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;

/**
 * Commandline terminal. Handles user interaction with the model.
 */
public class TerminalMode implements ProgramMode {


    private TerminalController terminalController;
    private MusicPlayer musicPlayer;


    /* default */

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
