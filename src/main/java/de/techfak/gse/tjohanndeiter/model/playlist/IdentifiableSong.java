package de.techfak.gse.tjohanndeiter.model.playlist;

import de.techfak.gse.tjohanndeiter.model.database.Song;

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
