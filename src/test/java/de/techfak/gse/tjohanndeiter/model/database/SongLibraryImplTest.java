package de.techfak.gse.tjohanndeiter.model.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class SongLibraryImplTest {


    private SongLibrary songLibrary;

    @BeforeEach
    void setUp() {
        songLibrary = new SongLibraryImpl("test");
        Assertions.assertDoesNotThrow(() -> songLibrary.addSong(new Song("test", "test", "test", "test", "test", 0)));
        Assertions.assertDoesNotThrow(() -> songLibrary.addSong(new Song("test2", "test2", "test2", "test2", "test2", 0)));
    }

    @Test
    void getAbsoluteFilepath() {
        Assertions.assertEquals("test", songLibrary.getAbsoluteFilepath());
    }

    @Test
    void addSong() {
        Assertions.assertDoesNotThrow(() -> songLibrary.addSong(new Song("test3", "test3", "test3", "test3", "test3", 1)));
    }

    @Test
    void getSong() {
        Assertions.assertEquals(new Song("test", "test", "test", "test", "test", 0), songLibrary.getSong(0));
        Assertions.assertEquals(new Song("test2", "test2", "test2", "test2", "test2", 0), songLibrary.getSong(1));
    }

    @Test
    void getSongs() {
        Assertions.assertEquals(List.of(new Song("test", "test", "test", "test", "test", 0),
                new Song("test2", "test2", "test2", "test2", "test2", 0)), songLibrary.getSongs());
    }
}