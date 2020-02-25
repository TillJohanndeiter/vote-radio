package de.techfak.gse.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.tjohanndeiter.model.player.TimeBean;

public class TimeBeanJsonParserImpl implements TimeBeanJsonParser {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public TimeBean toTimeBean(final String json) throws JsonProcessingException {
        return objectMapper.readValue(json, TimeBean.class);
    }

    @Override
    public String toJson(final TimeBean timeBean) throws JsonProcessingException {
        return objectMapper.writeValueAsString(timeBean);
    }
}
