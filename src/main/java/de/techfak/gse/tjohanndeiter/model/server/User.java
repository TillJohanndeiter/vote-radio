package de.techfak.gse.tjohanndeiter.model.server;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

/**
 * Represents a user form a client. {@link #jukebox} and {@link #client} are dummy users. Every user is identified by
 * {@link #ipAddress}.
 */
public class User {

    @JsonIgnore
    private static User jukebox = new User("LOCAL");
    @JsonIgnore
    private static User client = new User("CLIENT");

    private String ipAddress;

    /**
     * Required for json.
     */
    private User() {
    }

    public User(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public static User getJukebox() {
        return jukebox;
    }

    public static User getClient() {
        return client;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        final User user = (User) o;
        return Objects.equals(ipAddress, user.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress);
    }

    @Override
    public String toString() {
        return "User{"
                + "ipAddress='" + ipAddress
                + '\''
                + '}';
    }
}
