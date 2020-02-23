package de.techfak.gse.tjohanndeiter.model.exception.client;

import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ClientException;

public class UserDoesntExits extends ClientException {
    private static final long serialVersionUID = 42L;

    public UserDoesntExits(final String ipAddress) {
        super("User with Ip Address: " + ipAddress + " doesn't exits");
    }
}
