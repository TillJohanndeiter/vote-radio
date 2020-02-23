package de.techfak.gse.tjohanndeiter.model.voting;

import de.techfak.gse.tjohanndeiter.model.exception.client.UserVotedAlreadyException;
import de.techfak.gse.tjohanndeiter.model.exception.database.SongIdNotAvailable;
import de.techfak.gse.tjohanndeiter.model.server.User;

public interface VoteStrategy {

    void voteById(final int id, final User user) throws SongIdNotAvailable, UserVotedAlreadyException;
}
