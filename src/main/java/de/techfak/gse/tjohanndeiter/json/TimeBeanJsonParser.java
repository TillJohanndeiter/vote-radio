package de.techfak.gse.tjohanndeiter.json;

        import de.techfak.gse.tjohanndeiter.model.player.TimeBean;

public interface TimeBeanJsonParser {

    TimeBean toTimeBean(String json) throws JsonException;

    String toJson(TimeBean timeBean) throws JsonException;
}
