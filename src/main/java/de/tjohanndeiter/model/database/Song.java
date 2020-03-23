package de.tjohanndeiter.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

/**
 * Bean represents a mp3 file. Contains parsed metadata {@link #title}, {@link #artist}, {@link #album}, {@link #genre}
 * and {@link #length}. Also contains a {@link #cover}.
 */
public class Song { //NOPMD


    private static final String NO_METADATA = "---";

    private String filepath;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private long length;
    private byte[] cover;

    public Song() {

    }

    /**
     * Set Metadata of the parsed Song. If metadata is null {@link #NO_METADATA} is used as default.
     *
     * @param filepath filepath of the parsed *.mp3 file
     * @param title    title in metadata
     * @param artist   artist in metadata
     * @param album    album in metadata
     * @param genre    genre in metadata
     * @param length   length of song
     * @param cover    cover of song
     */
    public Song(final String filepath, final String title, final String artist, final String album,
                final String genre, final long length, final byte[] cover) {
        this.filepath = filepath;
        this.title = (title == null) ? NO_METADATA : title;
        this.artist = (artist == null) ? NO_METADATA : artist;
        this.album = (album == null) ? NO_METADATA : album;
        this.genre = (genre == null) ? NO_METADATA : genre;
        this.length = length;
        if (cover != null) {
            this.cover = cover.clone();
        }
    }


    public Song(final String filepath, final String title, final String artist, final String album,
                final String genre, final int length) {
        this(filepath, title, artist, album, genre, length, null);
    }

    /**
     * Copy Constructor.
     *
     * @param song song to copy.
     */
    public Song(final Song song) {
        this.filepath = song.filepath;
        this.title = song.title;
        this.artist = song.artist;
        this.album = song.album;
        this.genre = song.genre;
        this.length = song.length;
        this.cover = song.cover;
    }


    @Override
    public String toString() {
        return "Song{"
                + "title='" + title + '\''
                + "length =" + length + "s" + '\''
                + ", artist='" + artist + '\''
                + ", album='" + album + '\''
                + ", genre='" + genre + '\'' + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true; //NOPMD
        }
        if (!(o instanceof Song)) {
            return false; //NOPMD
        }
        final Song song = (Song) o;
        return Objects.equals(title, song.title)
                && Objects.equals(artist, song.artist)
                && Objects.equals(album, song.album)
                && Objects.equals(genre, song.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist, album, genre);
    }

    @JsonIgnore
    public String getFilepath() {
        return filepath;
    }

    public String getTitle() {
        return title;
    }


    public String getArtist() {
        return artist;
    }

    public long getLength() {
        return length;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public byte[] getCover() {
        return cover; //NOPMD
    }
}
