package de.techfak.gse.tjohanndeiter.mode.terminal;

import de.techfak.gse.tjohanndeiter.controller.cmd.TerminalController;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.player.OfflinePlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.ShuffleList;

/**
 * Commandline terminal. Handles user interaction with the model.
 */
public class TerminalMode implements ProgramMode {


    private final TerminalController terminalController;
    private SongLibrary songLibrary;
    private MusicPlayer musicPlayer;

    /**
     * Constructor
     *
     * @throws de.techfak.gse.tjohanndeiter.model.exception.prototypes.ParseException in case of non readable path
     */
    /* default */
    TerminalMode(final SongLibrary songLibrary) throws ShutdownException {
        this.songLibrary = songLibrary;
        final Playlist playlist = new ShuffleList(songLibrary);
        musicPlayer = new OfflinePlayer(playlist);
        terminalController = new TerminalController(playlist);
        Runtime.getRuntime().addShutdownHook(new Thread(musicPlayer::end));
    }

    @Override
    public void startProgram() {
        System.out.println(songLibrary.toString()); //NOPMD
        musicPlayer.startPlay();
        terminalController.inputLoop();
    }
}
