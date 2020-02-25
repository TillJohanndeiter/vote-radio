package de.techfak.gse.tjohanndeiter.model.voting;

import de.techfak.gse.tjohanndeiter.exception.client.UserVotedAlreadyException;
import de.techfak.gse.tjohanndeiter.exception.database.SongIdNotAvailable;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.Vote;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.mode.server.User;

/**
 * VoteStrategy for server. Vote is only hand over to {@link VoteList} if user hasn't vote for song already.
 */
public class ServerStrategy implements VoteStrategy {

    private final VoteList voteList;

    public ServerStrategy(final VoteList voteList) {
        this.voteList = voteList;
    }

    /**
     * Only hand over vote to {@link VoteList} if #user didn't voted already for song with #id.
     *
     * @param id   of song
     * @param user user who vote for song
     * @throws SongIdNotAvailable        if id doesn't belong to any
     *                                   {@link de.techfak.gse.tjohanndeiter.model.database.Song}
     * @throws UserVotedAlreadyException if user voted already for song with id
     */
    @Override
    public void voteById(final int id, final User user) throws SongIdNotAvailable, UserVotedAlreadyException {

        final QueueSong queueSong = voteList.findSongById(id);

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
