package de.techfak.gse.tjohanndeiter.model.voting;

import de.techfak.gse.tjohanndeiter.model.exception.database.SongIdNotAvailable;

public interface VoteStrategy {
    void voteById(int id) throws SongIdNotAvailable;

}
