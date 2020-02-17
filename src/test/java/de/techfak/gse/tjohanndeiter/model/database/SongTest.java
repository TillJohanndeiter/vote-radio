package de.techfak.gse.tjohanndeiter.model.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SongTest {


    private static final String NO_METADATA = "---";
    private static final String FP_TEST = "fpTest";
    private static final String TITLE_TEST = "titleTest";
    private static final String ARTIST_TEST = "artistTest";
    private static final String ALBUM_TEST = "albumTest";
    private static final String GENRE_TEST = "genreTest";
    private Song song = new Song(FP_TEST, TITLE_TEST, ARTIST_TEST, ALBUM_TEST, GENRE_TEST, 5);

    @Test
    void getFilepath() {
        Assertions.assertEquals(song.getFilepath(), FP_TEST);
    }

    @Test
    void getTitle() {
        Assertions.assertEquals(song.getTitle(), TITLE_TEST);
    }

    @Test
    void getArtist() {
        Assertions.assertEquals(song.getArtist(), ARTIST_TEST);
    }

    @Test
    void getLength() {
        Assertions.assertEquals(song.getLength(), 5);
    }

    @Test
    void getAlbum() {
        Assertions.assertEquals(song.getAlbum(), ALBUM_TEST);
    }

    @Test
    void getGenre() {
        Assertions.assertEquals(song.getGenre(), GENRE_TEST);
    }


    private Song defaultSong = new Song(null, null, null, null, null, 0);

    @Test
    void getFilepathDefault() {
        Assertions.assertEquals(defaultSong.getFilepath(), NO_METADATA);
    }

    @Test
    void getTitleDefault() {
        Assertions.assertEquals(defaultSong.getTitle(), NO_METADATA);
    }

    @Test
    void getArtistDefault() {
        Assertions.assertEquals(defaultSong.getArtist(), NO_METADATA);
    }

    @Test
    void getLengthDefault() {
        Assertions.assertEquals(defaultSong.getLength(), 0);
    }

    @Test
    void getAlbumDefault() {
        Assertions.assertEquals(defaultSong.getAlbum(), NO_METADATA);
    }

    @Test
    void getGenreDefault() {
        Assertions.assertEquals(defaultSong.getGenre(), NO_METADATA);
    }
}