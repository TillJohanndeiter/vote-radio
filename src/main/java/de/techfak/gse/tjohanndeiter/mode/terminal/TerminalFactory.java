package de.techfak.gse.tjohanndeiter.mode.terminal;

import de.techfak.gse.tjohanndeiter.controller.cmd.TerminalController;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.mode.ProgramModeFactory;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;
import de.techfak.gse.tjohanndeiter.model.database.SongLibraryFactory;
import de.techfak.gse.tjohanndeiter.model.database.SongLibraryVlcJFactory;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.player.OfflinePlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.ShuffleList;

import java.io.File;

public class TerminalFactory extends ProgramModeFactory {

    private SongLibraryFactory factory = new SongLibraryVlcJFactory();

    @Override
    public ProgramMode createProgramMode(final String... args) throws ShutdownException {
        checkIfIllegalArgCombination(args);
        final String filepath = parseFilepath(args, 0);
        final SongLibrary songLibrary = factory.createSongLibrary(new File(filepath));
        final Playlist playlist = new ShuffleList(songLibrary);
        final MusicPlayer musicPlayer = new OfflinePlayer(playlist);
        TerminalController terminalController = new TerminalController();
        addObservers(playlist, musicPlayer, terminalController);
        Runtime.getRuntime().addShutdownHook(new Thread(musicPlayer::end));
        return new TerminalMode(musicPlayer, terminalController);
    }

    private void addObservers(final Playlist playlist, final MusicPlayer musicPlayer,
                              final TerminalController terminalController) {
        playlist.addPropertyChangeListener(terminalController);
        musicPlayer.addPropertyChangeListener(terminalController);
    }
}
