package de.techfak.gse.tjohanndeiter.model.server;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class User {

    @JsonIgnore
    public static User JUKEBOX = new User("LOCAL");
    @JsonIgnore
    public static User CLIENT = new User("CLIENT");

    private String ipAddress;

    private User() {
    }

    public User(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        final User user = (User) o;
        return Objects.equals(ipAddress, user.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress);
    }

    @Override
    public String toString() {
        return "User{" +
                "ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
