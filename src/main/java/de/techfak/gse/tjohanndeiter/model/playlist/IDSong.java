package de.techfak.gse.tjohanndeiter.model.playlist;

import de.techfak.gse.tjohanndeiter.model.database.Song;

class IDSong extends Song {

    protected int id;

    IDSong() {
        super();
    }


    IDSong(final Song song, final int id) {
        super(song);
        this.id = id;
    }

}
