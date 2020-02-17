package de.techfak.gse.tjohanndeiter.model.playlist;

import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Playlist returns a random song from the playlist.
 */
public class ShuffleList extends Playlist {


    //TODO: Refactor to iterator
    private List<Song> playList;
    private int idxCurrentSong;

    /**
     * Calls super an make a copy of {@link Song} in {@link SongLibrary}.
     *
     * @param songLibrary contains all songs used in playlist
     */
    public ShuffleList(final SongLibrary songLibrary) {
        super();
        playList = new ArrayList<>(songLibrary.getSongs());
        shuffle();
    }

    @Override
    public Song getNextSong() {
        final int oldIdx = idxCurrentSong;
        if (idxCurrentSong < playList.size() - 1) {
            idxCurrentSong++;
        } else {
            idxCurrentSong = 0;
        }
        propertyChangeSupport.firePropertyChange("newSong", playList.get(idxCurrentSong),
                playList.get(oldIdx));
        return playList.get(idxCurrentSong);
    }

    @Override
    public Song getCurrentSong() {
        return playList.get(idxCurrentSong);
    }

    @Override
    public void addSong(final Song song) {
        playList.add(song);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (int i = idxCurrentSong; i < playList.size(); i++) {
            result.append(playList.get(i).toString());
            result.append('\n');
        }
        for (int i = 0; i < idxCurrentSong; i++) {
            result.append(playList.get(i).toString());
            result.append('\n');
        }
        result.append("Playlist start from the begin");
        return result.toString();
    }

    private void shuffle() {
        Collections.shuffle(playList);
    }
}

