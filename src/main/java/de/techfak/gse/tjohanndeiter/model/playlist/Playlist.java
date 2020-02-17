package de.techfak.gse.tjohanndeiter.model.playlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.techfak.gse.tjohanndeiter.model.database.Song;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Abstract representation for a list of {@link Song} objects.
 */

/**
 * Abstract representation for a list of {@link Song} objects.
 */
public abstract class Playlist {

    public static final String PLAYLIST_CHANGE = "PLAYLIST_CHANGED";
    public static final String NEW_SONG = "NEW_SONG";

    PropertyChangeSupport propertyChangeSupport;

    Playlist() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    @JsonIgnore
    public abstract Song getNextSong();

    @JsonIgnore
    public abstract Song getCurrentSong();

    public void addPropertyChangeListener(final PropertyChangeListener observer) {
        propertyChangeSupport.addPropertyChangeListener(observer);
    }

    public abstract void addSong(final Song song);
}
