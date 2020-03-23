package de.tjohanndeiter.model.voting;

import de.tjohanndeiter.mode.server.User;

public class Vote { //NOPMD

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
