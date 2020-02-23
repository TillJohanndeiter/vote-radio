package de.techfak.gse.tjohanndeiter.model.voting;

import de.techfak.gse.tjohanndeiter.model.exception.client.UserVotedAlreadyException;
import de.techfak.gse.tjohanndeiter.model.exception.database.SongIdNotAvailable;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.Vote;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.server.User;

public class ServerStrategy implements VoteStrategy{

    private VoteList voteList;

    public ServerStrategy(final VoteList voteList) {
        this.voteList = voteList;
    }

    public void voteById(final int id, final User user) throws SongIdNotAvailable, UserVotedAlreadyException {

        QueueSong queueSong = voteList.findSongById(id);

        if (userNotVotedForSong(queueSong, user)) {
            voteList.voteForSongById(id, user);
        } else {
            throw new UserVotedAlreadyException(user, queueSong);
        }
    }

    private boolean userNotVotedForSong(final QueueSong queueSong, final User user) {
        for (final Vote vote : queueSong.getVotes()) {
            if (vote.getUser().equals(user)) {
                return false;
            }
        }

        return true;
    }
}
