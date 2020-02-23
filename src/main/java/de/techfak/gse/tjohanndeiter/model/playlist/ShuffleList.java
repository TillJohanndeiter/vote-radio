package de.techfak.gse.tjohanndeiter.model.playlist;

import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;

import java.util.Collections;

/**
 * Playlist returns a random song from the playlist.
 */
public class ShuffleList extends Playlist {

    /**
     * Calls super an make a copy of {@link Song} in {@link SongLibrary}.
     *
     * @param songLibrary contains all songs used in playlist
     */
    public ShuffleList(final SongLibrary songLibrary) {
        super(songLibrary);
        shuffle();
    }


    /**
     * Takes last element of list and moves it to first position in {@link #songList}.
     */
    @Override
    public void skipToNext() {
        songList.add(songList.remove());
        support.firePropertyChange(PLAYLIST_CHANGE, null, this);
    }


    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (final Song song : songList) {
            result.append(song.toString()).append('\n');
        }
        result.append(" -- End of Playlist");
        return result.toString();
    }

    private void shuffle() {
        Collections.shuffle(songList);
    }
}

