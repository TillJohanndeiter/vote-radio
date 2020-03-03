package de.techfak.gse.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.tjohanndeiter.mode.server.User;

public class UserJsonParserImpl implements UserJsonParser {


    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public User toUser(final String json) throws JsonException {
        try {
            return objectMapper.readValue(json, User.class);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to deserialize User", e);
        }
    }

    @Override
    public String toJson(final User user) throws JsonException {
        try {

            return objectMapper.writeValueAsString(user);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to serialize User", e);
        }
    }
}