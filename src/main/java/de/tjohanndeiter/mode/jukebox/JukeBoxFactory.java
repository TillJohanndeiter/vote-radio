package de.tjohanndeiter.mode.jukebox;

import de.tjohanndeiter.exception.prototypes.ShutdownException;
import de.tjohanndeiter.mode.ProgramMode;
import de.tjohanndeiter.mode.ProgramModeFactory;
import de.tjohanndeiter.model.database.SongLibrary;
import de.tjohanndeiter.model.player.MusicPlayer;
import de.tjohanndeiter.model.player.OfflinePlayer;
import de.tjohanndeiter.model.playlist.VoteList;
import de.tjohanndeiter.model.voting.JukeBoxStrategy;
import de.tjohanndeiter.model.voting.VoteStrategy;


/**
 * Factory for {@link JukeBoxMode}.
 * If no filepath is available in #args the current path of program is selected as music folder.
 */
public class JukeBoxFactory extends ProgramModeFactory {


    public static final int PLAYS_BEFORE_REPLAY_DEFAULT = 3;

    /**
     * Checks if #args are contradictory and if not creates {@link JukeBoxMode}.
     *
     * @param args Cmd args
     * @return created {@link JukeBoxMode}
     * @throws ShutdownException if #args contains contradictory combinations.
     */
    @Override
    public ProgramMode createSpecificProgramMode(final String... args) throws ShutdownException {
        final SongLibrary songLibrary = createSongLibrary(args, 1);
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
}
