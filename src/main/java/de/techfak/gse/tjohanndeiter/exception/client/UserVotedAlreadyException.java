package de.techfak.gse.tjohanndeiter.exception.client;

import de.techfak.gse.tjohanndeiter.exception.prototypes.ClientException;
import de.techfak.gse.tjohanndeiter.model.playlist.VotedSong;
import de.techfak.gse.tjohanndeiter.mode.server.User;

/**
 * Throw in case of a user try to vote more than one time for one song.
 */
public class UserVotedAlreadyException extends ClientException {

    private static final long serialVersionUID = 42L;

    public UserVotedAlreadyException(final User user, final VotedSong queueSong) {
        super("User: " + user.getIpAddress() + " voted already for: " + queueSong.getTitle());
    }
}
