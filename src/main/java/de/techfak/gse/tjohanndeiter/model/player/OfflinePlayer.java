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
                    new EndEvent().run();
                    mediaPlayer.media().play(playlist.getCurrentSong().getFilepath());
                }));
            }
        });
    }


}
