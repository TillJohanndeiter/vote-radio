package de.techfak.gse.tjohanndeiter.json;

import de.techfak.gse.tjohanndeiter.mode.server.User;

public interface UserJsonParser {
    String toJson(User song) throws JsonException;

    User toUser(String json) throws JsonException;
}
