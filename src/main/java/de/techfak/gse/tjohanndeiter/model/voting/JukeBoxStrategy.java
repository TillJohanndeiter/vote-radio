package de.techfak.gse.tjohanndeiter.model.voting;

import de.techfak.gse.tjohanndeiter.model.exception.database.SongIdNotAvailable;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.server.User;

public class JukeBoxStrategy implements VoteStrategy {

    private VoteList voteList;

    public JukeBoxStrategy(final VoteList voteList) {
        this.voteList = voteList;
    }

    @Override
    public void voteById(final int id, final User user) throws SongIdNotAvailable {
        voteList.voteForSongById(id, user);
    }
}
