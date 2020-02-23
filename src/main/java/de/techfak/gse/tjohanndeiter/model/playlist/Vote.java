package de.techfak.gse.tjohanndeiter.model.playlist;

import de.techfak.gse.tjohanndeiter.model.server.User;

public class Vote {

    private User user;

    private Vote() {
    }

    public Vote(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
