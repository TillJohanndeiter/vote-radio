package de.techfak.gse.tjohanndeiter.model.exception.client;

import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ClientException;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.server.User;

/**
 * Throw in case of a user try to vote more than one time for one song.
 */
public class UserVotedAlreadyException extends ClientException {

    private static final long serialVersionUID = 42L;

    public UserVotedAlreadyException(final User user, final QueueSong queueSong) {
        super("User: " + user.getIpAddress() + " voted already for: " + queueSong.getTitle());
    }
}
