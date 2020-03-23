package de.tjohanndeiter.model.database;

import de.tjohanndeiter.exception.database.SongAlreadyExitsException;

import java.util.List;

/**
 * Storage of all parsed songs.
 */
public interface SongLibrary {

    void addSong(Song song) throws SongAlreadyExitsException;

    Song getSong(int idOfSong);

    List<Song> getSongs();

    String getAbsoluteFilepath();

    @Override
    String toString();
}
