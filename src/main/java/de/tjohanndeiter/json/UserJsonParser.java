package de.tjohanndeiter.json;

import de.tjohanndeiter.mode.server.User;

public interface UserJsonParser {
    String toJson(User song) throws JsonException;

    User toUser(String json) throws JsonException;
}
