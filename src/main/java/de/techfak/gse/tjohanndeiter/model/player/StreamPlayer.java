package de.techfak.gse.tjohanndeiter.model.player;

import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;

public class StreamPlayer extends MusicPlayer {

    private String address;
    private MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    private MediaListPlayer listPlayer = mediaPlayerFactory.mediaPlayers().newMediaListPlayer();
    private Playlist playlist;


    public StreamPlayer(final Playlist playlist, final String multicastAddress, final int port) {
        this.playlist = playlist;
        final MediaListRef mediaList = mediaPlayerFactory.media().newMediaListRef();
        listPlayer.list().setMediaList(mediaList);
        address = formatRtpStream(multicastAddress, port);
        this.listPlayer.events().addMediaListPlayerEventListener(new MediaListPlayerEventAdapter() {
            @Override
            public void mediaListPlayerFinished(final MediaListPlayer mediaPlayer) {
                mediaPlayer.submit(() -> {
                    mediaPlayer.list().media().clear();
                    mediaPlayer.list().media().add(playlist.getNextSong().getFilepath(), address);
                    mediaPlayer.controls().play();
                });
            }
        });
    }

    @Override
    public void startPlay() {
        listPlayer.list().media().add(playlist.getNextSong().getFilepath(), address);
        listPlayer.controls().play();
    }

    private String formatRtpStream(final String multicastAddress, final int musicPort) {
        return ":sout=#rtp{dst="
                + multicastAddress
                + ",port="
                + musicPort
                + ",mux=ts}";
    }
}
