package de.tjohanndeiter.model.voting;

import de.tjohanndeiter.exception.database.SongIdNotAvailable;
import de.tjohanndeiter.model.playlist.VoteList;
import de.tjohanndeiter.mode.server.User;

/**
 * Simple strategy of a client. Hand over #id of vote to {@link VoteList}.
 */
public class JukeBoxStrategy implements VoteStrategy {

    private final VoteList voteList;

    public JukeBoxStrategy(final VoteList voteList) {
        this.voteList = voteList;
    }

    @Override
    public void voteById(final int id, final User user) throws SongIdNotAvailable {
        voteList.voteForSongById(id, user);
    }
}
