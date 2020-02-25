package de.techfak.gse.tjohanndeiter.model.voting;

import de.techfak.gse.tjohanndeiter.exception.client.UserVotedAlreadyException;
import de.techfak.gse.tjohanndeiter.exception.database.SongIdNotAvailable;
import de.techfak.gse.tjohanndeiter.mode.server.User;

public interface VoteStrategy {

    void voteById(final int id, final User user) throws SongIdNotAvailable, UserVotedAlreadyException;
}
