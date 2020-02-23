package de.techfak.gse.tjohanndeiter.model.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

class JsonQueuedSongTest {

    private static final String NO_METADATA = "---";
    private static final String FP_TEST = "fpTest";
    private static final String TITLE_TEST = "titleTest";
    private static final String ARTIST_TEST = "artistTest";
    private static final String ALBUM_TEST = "albumTest";
    private static final String GENRE_TEST = "genreTest";
    private static final String JSON_DEFAULT_SONG = "{\"title\":\"---\",\"artist\":\"---\","
            + "\"album\":\"---\",\"genre\":\"---\",\"length\":2,\"cover\":null,\"votes\":[],"
            + "\"playsBeforeReplay\":0,\"id\":3}";

    private static final String NORMAL_JSON_SONG = "{\"title\":\"titleTest\",\"artist\":\"artistTest\","
            + "\"album\":\"albumTest\",\"genre\":\"genreTest\",\"length\":5,\"cover\":null,"
            + "\"votes\":[],\"playsBeforeReplay\":0,\"id\":1}";


    private QueueSong queueSong = new QueueSong(
            new Song(FP_TEST, TITLE_TEST, ARTIST_TEST, ALBUM_TEST, GENRE_TEST, 5), 1, 0);
    private QueueSong defaultSong = new QueueSong(
            new Song(NO_METADATA, NO_METADATA, NO_METADATA, NO_METADATA, NO_METADATA, 2), 3, 0);

    private QueueSongJsonParser songJsonParser = new QueueSongJsonParserImpl();

    @Test
    void normalSerialization() {
        final AtomicReference<String> json = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> json.set(songJsonParser.toJson(queueSong)));
        Assertions.assertEquals(
                NORMAL_JSON_SONG,
                json.get());
    }

    @Test
    void defaultSerialization() {
        final AtomicReference<String> json = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> json.set(songJsonParser.toJson(defaultSong)));
        Assertions.assertEquals(
                JSON_DEFAULT_SONG,
                json.get());
    }

    @Test
    void toJsonNormal() throws JsonProcessingException {
        QueueSong result = songJsonParser.toSong(NORMAL_JSON_SONG);
        Assertions.assertEquals(result, queueSong);
    }

    @Test
    void toJsonDefault() throws JsonProcessingException {
        QueueSong result = songJsonParser.toSong(JSON_DEFAULT_SONG);
        Assertions.assertEquals(result, defaultSong);
    }

    @Test
    void notSerializable() throws JsonProcessingException {
        String playlistJson = new VoteListJsonParserImpl().toJson(new VoteList(List.of(queueSong, defaultSong)));

        Assertions.assertThrows(JsonProcessingException.class, () -> songJsonParser.toSong(playlistJson));
    }

}