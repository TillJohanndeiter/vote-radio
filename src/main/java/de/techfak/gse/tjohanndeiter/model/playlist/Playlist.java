package de.techfak.gse.tjohanndeiter.model.playlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

/**
 * Abstract representation for a list of {@link Song} objects.
 */

/**
 * Abstract representation for a list of {@link Song} objects.
 */
public abstract class Playlist {

    public static final String PLAYLIST_CHANGE = "PLAYLIST_CHANGED";

    LinkedList<Song> songList;

    PropertyChangeSupport support = new PropertyChangeSupport(this);

    Playlist() {
    }

    Playlist(final SongLibrary songLibrary) {
        songList = new LinkedList<>(songLibrary.getSongs());
    }

    @JsonIgnore
    public abstract void skipToNext();

    @JsonIgnore
    public Song getCurrentSong() {
        return songList.get(0);
    }

    public void addPropertyChangeListener(final PropertyChangeListener observer) {
        support.addPropertyChangeListener(observer);
    }

    public void addSong(final Song song) {
        songList.add(song);
        support.firePropertyChange(PLAYLIST_CHANGE, null, this);
    }

}
