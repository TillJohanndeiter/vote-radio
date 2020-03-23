package de.tjohanndeiter.exception.client;

import de.tjohanndeiter.exception.prototypes.ClientException;

/**
 * Thrown in case of a user request by ip address that doesn't exits yet.
 */
public class UserDoesntExits extends ClientException {
    private static final long serialVersionUID = 42L;

    public UserDoesntExits(final String ipAddress) {
        super("User with Ip Address: " + ipAddress + " doesn't exits");
    }
}
