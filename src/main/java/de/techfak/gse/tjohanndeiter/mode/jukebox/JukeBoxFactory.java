package de.techfak.gse.tjohanndeiter.mode.jukebox;

import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.mode.ProgramModeFactory;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;
import de.techfak.gse.tjohanndeiter.model.database.SongLibraryFactory;
import de.techfak.gse.tjohanndeiter.model.database.SongLibraryVlcJFactory;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.player.OfflinePlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.voting.JukeBoxStrategy;
import de.techfak.gse.tjohanndeiter.model.voting.VoteStrategy;

import java.io.File;


/**
 * Factory for {@link JukeBoxMode}.
 * If no filepath is available in #args the current path of program is selected as music folder.
 */
public class JukeBoxFactory extends ProgramModeFactory {


    public static final int PLAYS_BEFORE_REPLAY_DEFAULT = 3;

    /**
     * Checks if #args are contradictory and if not creates {@link JukeBoxMode}.
     * @param args Cmd args
     * @return created {@link JukeBoxMode}
     * @throws ShutdownException if #args contains contradictory combinations.
     */
    @Override
    public ProgramMode createProgramMode(final String... args) throws ShutdownException {
        checkIfIllegalArgCombination(args);
        final SongLibrary songLibrary = createSongLibrary(args);
        final VoteList voteList = setVoteList(songLibrary);
        setMusicPlayer(voteList);
        setVoteStrategy(voteList);
        return new JukeBoxMode();
    }

    private VoteList setVoteList(final SongLibrary songLibrary) {
        final VoteList voteList = new VoteList(songLibrary, PLAYS_BEFORE_REPLAY_DEFAULT);
        JukeBoxMode.setVoteList(voteList);
        return voteList;
    }

    private void setMusicPlayer(final VoteList voteList) {
        final MusicPlayer musicPlayer = new OfflinePlayer(voteList);
        JukeBoxMode.setMusicPlayer(musicPlayer);
    }

    private void setVoteStrategy(final VoteList voteList) {
        final VoteStrategy voteStrategy = new JukeBoxStrategy(voteList);
        JukeBoxMode.setVoteStrategy(voteStrategy);
    }

    private SongLibrary createSongLibrary(final String[] args) throws ShutdownException {
        final String filepath = parseFilepath(args, 1);
        final SongLibraryFactory songLibraryFactory = new SongLibraryVlcJFactory();
        return songLibraryFactory.createSongLibrary(new File(filepath));
    }
}
