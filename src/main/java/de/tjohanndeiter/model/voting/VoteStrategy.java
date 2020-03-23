package de.tjohanndeiter.model.voting;

import de.tjohanndeiter.exception.client.UserVotedAlreadyException;
import de.tjohanndeiter.exception.database.SongIdNotAvailable;
import de.tjohanndeiter.mode.server.User;

public interface VoteStrategy {

    void voteById(final int id, final User user) throws SongIdNotAvailable, UserVotedAlreadyException;
}
