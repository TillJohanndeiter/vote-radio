package de.techfak.gse.tjohanndeiter.model.voting;

import de.techfak.gse.tjohanndeiter.model.client.Client;
import de.techfak.gse.tjohanndeiter.model.server.User;

/**
 * Simple strategy of a client. Hand over #id of vote to client. Client make http request to server to vote.
 */
public class ClientStrategy implements VoteStrategy {

    private Client client;

    public ClientStrategy(final Client client) {
        this.client = client;
    }

    @Override
    public void voteById(final int id, final User user) {
        client.voteById(id);
    }
}
