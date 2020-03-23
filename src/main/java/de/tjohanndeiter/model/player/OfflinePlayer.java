package de.tjohanndeiter.model.player;

import de.tjohanndeiter.model.database.Song;
import de.tjohanndeiter.model.playlist.Playlist;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

/**
 * Implements {@linkplain MusicPlayer}. Using the vlcJ library.
 */
public class OfflinePlayer extends MusicPlayer {

    public OfflinePlayer(final Playlist playlist) {
        super(playlist);
        setFinishedEvent();
    }

    @Override
    public void startPlay()  {
        mediaPlayer.media().prepare(playlist.getCurrentSong().getFilepath());
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
                    support.firePropertyChange(NEW_SONG, null, song);
                }));
            }
        });
    }


}
