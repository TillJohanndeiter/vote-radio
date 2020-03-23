package de.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tjohanndeiter.model.player.TimeBean;

public class TimeBeanJsonParserImpl implements TimeBeanJsonParser {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public TimeBean toTimeBean(final String json) throws JsonException {
        try {
            return objectMapper.readValue(json, TimeBean.class);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to deserialize TimeBean", e);
        }
    }

    @Override
    public String toJson(final TimeBean timeBean) throws JsonException {
        try {
            return objectMapper.writeValueAsString(timeBean);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to serialize TimeBean", e);
        }
    }
}
