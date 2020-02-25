package de.techfak.gse.tjohanndeiter.mode.terminal;

import de.techfak.gse.tjohanndeiter.controller.cmd.TerminalController;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.mode.ProgramModeFactory;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;
import de.techfak.gse.tjohanndeiter.model.database.SongLibraryFactory;
import de.techfak.gse.tjohanndeiter.model.database.SongLibraryVlcJFactory;
import de.techfak.gse.tjohanndeiter.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.player.OfflinePlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.ShuffleList;

import java.io.File;

/**
 * Factory for {@link TerminalMode}. Parse filepath an creates random play with it.
 * If no filepath is available in #args the current path of program is selected as music folder.
 */
public class TerminalFactory extends ProgramModeFactory {

    private final SongLibraryFactory factory = new SongLibraryVlcJFactory();

    /**
     * Parse filepath and creates Terminal mode.
     * @param args args from cmd optional contains a
     * @return created terminal mode
     * @throws ShutdownException if folder doesn't contain mp3 files or argument combination is illegal
     */
    @Override
    public ProgramMode createProgramMode(final String... args) throws ShutdownException {
        checkIfIllegalArgCombination(args);
        final String filepath = parseFilepath(args, 0);
        final SongLibrary songLibrary = factory.createSongLibrary(new File(filepath));
        final Playlist playlist = new ShuffleList(songLibrary);
        final MusicPlayer musicPlayer = new OfflinePlayer(playlist);
        final TerminalController terminalController = new TerminalController();
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
