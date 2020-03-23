package de.tjohanndeiter.mode.server;

import de.tjohanndeiter.exception.client.UserDoesntExits;

import java.util.ArrayList;
import java.util.List;

/**
 * Storage of all currently active users and clients.
 */
public class UserManger {

    private final List<User> users = new ArrayList<>();

    void addUser(final User user) {
        users.add(user);
    }

     User getUserByIp(final String ipAddress) throws UserDoesntExits {
        return findUserByIp(ipAddress);
    }

    void removeUserByIp(final String ipAddress) throws UserDoesntExits {
        final User toDelete = findUserByIp(ipAddress);
        users.remove(toDelete);
    }

    private User findUserByIp(final String ipAddress) throws UserDoesntExits {
        for (final User user : users) {
            if (user.getIpAddress().equals(ipAddress)) {
                return user;
            }
        }
        throw new UserDoesntExits(ipAddress);
    }
}
