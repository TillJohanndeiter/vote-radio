package de.tjohanndeiter.model.voting;

import de.tjohanndeiter.exception.client.UserVotedAlreadyException;
import de.tjohanndeiter.exception.database.SongIdNotAvailable;
import de.tjohanndeiter.model.playlist.VoteList;
import de.tjohanndeiter.mode.server.User;
import de.tjohanndeiter.model.playlist.VotedSong;

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
     *                                   {@link de.tjohanndeiter.model.database.Song}
     * @throws UserVotedAlreadyException if user voted already for song with id
     */
    @Override
    public void voteById(final int id, final User user) throws SongIdNotAvailable, UserVotedAlreadyException {

        final VotedSong queueSong = voteList.findSongById(id);

        if (userNotVotedForSong(queueSong, user)) {
            voteList.voteForSongById(id, user);
        } else {
            throw new UserVotedAlreadyException(user, queueSong);
        }
    }

    private boolean userNotVotedForSong(final VotedSong queueSong, final User user) {
        for (final Vote vote : queueSong.getVotes()) {
            if (vote.getUser().equals(user)) {
                return false;
            }
        }

        return true;
    }
}
