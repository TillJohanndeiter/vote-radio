package de.tjohanndeiter.model.playlist;

import de.tjohanndeiter.model.database.Song;

class IdentifiableSong extends Song {

    protected int id;

    IdentifiableSong() {
        super();
    }


    IdentifiableSong(final Song song, final int id) {
        super(song);
        this.id = id;
    }

}
