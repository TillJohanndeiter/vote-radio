package de.tjohanndeiter.mode.terminal;

import de.tjohanndeiter.controller.cmd.TerminalController;
import de.tjohanndeiter.exception.prototypes.ShutdownException;
import de.tjohanndeiter.mode.ProgramMode;
import de.tjohanndeiter.mode.ProgramModeFactory;
import de.tjohanndeiter.model.database.SongLibrary;
import de.tjohanndeiter.model.database.SongLibraryFactory;
import de.tjohanndeiter.model.database.SongLibraryFactoryImpl;
import de.tjohanndeiter.model.player.MusicPlayer;
import de.tjohanndeiter.model.player.OfflinePlayer;
import de.tjohanndeiter.model.playlist.Playlist;
import de.tjohanndeiter.model.playlist.ShuffleList;

/**
 * Factory for {@link TerminalMode}. Parse filepath an creates random play with it.
 * If no filepath is available in #args the current path of program is selected as music folder.
 */
public class TerminalFactory extends ProgramModeFactory {

    private final SongLibraryFactory factory = new SongLibraryFactoryImpl();

    /**
     * Parse filepath and creates Terminal mode.
     * @param args args from cmd optional contains a
     * @return created terminal mode
     * @throws ShutdownException if folder doesn't contain mp3 files or argument combination is illegal
     */
    @Override
    public ProgramMode createSpecificProgramMode(final String... args) throws ShutdownException {
        final SongLibrary songLibrary = createSongLibrary(args, 0);
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
