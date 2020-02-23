package de.techfak.gse.tjohanndeiter.model.server;

import de.techfak.gse.tjohanndeiter.model.exception.client.UserDoesntExits;

import java.util.ArrayList;
import java.util.List;

public class UserManger {

    private List<User> users = new ArrayList<>();

    public void addUser(final User user) {
        users.add(user);
    }

    public User getUserByIp(final String ipAddress) throws UserDoesntExits {
        return findUserByIp(ipAddress);
    }

    public void removeUserByIp(final String ipAddress) throws UserDoesntExits {
        User toDelete = findUserByIp(ipAddress);
        users.remove(toDelete);
    }

    private User findUserByIp(final String ipAddress) throws UserDoesntExits {
        for (final User user : users) {
            if (user.getIpAddress().equals(ipAddress)){
                return user;
            }
        }
        throw new UserDoesntExits(ipAddress);
    }
}
