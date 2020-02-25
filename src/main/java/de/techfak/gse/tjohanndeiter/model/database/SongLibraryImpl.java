package de.techfak.gse.tjohanndeiter.model.database;

import de.techfak.gse.tjohanndeiter.exception.database.SongAlreadyExitsException;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements song storage with a {@linkplain ArrayList}.
 */
public class SongLibraryImpl implements SongLibrary {

    private final List<Song> songs = new ArrayList<>();

    private final String absFilePath;

    SongLibraryImpl(final String absFilePath) {
        this.absFilePath = absFilePath;
    }


    @Override
    public String getAbsoluteFilepath() {
        return absFilePath;
    }

    @Override
    public void addSong(final Song song) throws SongAlreadyExitsException {
        if (songs.contains(song)) {
            throw new SongAlreadyExitsException(song);
        }
        songs.add(song);
    }


    @Override
    public Song getSong(final int idOfSong) {
        return songs.get(idOfSong);
    }

    @Override
    public List<Song> getSongs() {
        return songs;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (final Song song : songs) {
            result.append(song.toString()).append('\n');
            result.append('\n');
        }

        return result.toString();
    }
}
