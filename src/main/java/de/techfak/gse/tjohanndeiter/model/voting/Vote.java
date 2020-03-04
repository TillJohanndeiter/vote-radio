package de.techfak.gse.tjohanndeiter.model.voting;

import de.techfak.gse.tjohanndeiter.mode.server.User;

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
