package de.techfak.gse.tjohanndeiter.model.player;

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
        mediaPlayer.submit(() -> mediaPlayer.media().prepare(playlist.getNextSong().getFilepath()));
    }

    @Override
    public void startPlay()  {
        super.startPlay();
        mediaPlayer.submit(() -> mediaPlayer.media().play(playlist.getNextSong().getFilepath()));
    }

    private void setFinishedEvent() {
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                mediaPlayer.submit(() -> mediaPlayer.media().play(playlist.getNextSong().getFilepath()));
            }
        });
    }


}
