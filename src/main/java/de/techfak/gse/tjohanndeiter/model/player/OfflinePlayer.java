package de.techfak.gse.tjohanndeiter.model.player;

import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

/**
 * Implements {@linkplain MusicPlayer}. Using the vlcJ library.
 */
public class OfflinePlayer extends MusicPlayer {


    private Playlist playlist;

    public OfflinePlayer(final Playlist playlist) {
        this.playlist = playlist;
        setFinishedEvent();
    }

    @Override
    public void startPlay()  {
        final Song song = playlist.getCurrentSong();
        propertyChangeSupport.firePropertyChange(NEW_SONG, null, song);
        propertyChangeSupport.firePropertyChange(Playlist.PLAYLIST_CHANGE, null, playlist);
        mediaPlayer.media().play(playlist.getCurrentSong().getFilepath());
        super.startPlay();
    }

    private void setFinishedEvent() {
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                mediaPlayer.submit(new Thread(() -> {
                    playlist.skipToNext();
                    final Song song = playlist.getCurrentSong();
                    mediaPlayer.media().play(song.getFilepath());
                    propertyChangeSupport.firePropertyChange(NEW_SONG, null, song);
                }));
            }
        });
    }


}
