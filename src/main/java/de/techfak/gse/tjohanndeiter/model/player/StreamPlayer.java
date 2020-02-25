package de.techfak.gse.tjohanndeiter.model.player;

import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.mode.server.StreamUrl;
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class StreamPlayer extends MusicPlayer {

    private final String address;
    private final MediaListPlayer listPlayer = mediaPlayerFactory.mediaPlayers().newMediaListPlayer();
    private final Timer timer = new Timer();
    private Song currentSong;
    private TimerTask timerTask;
    private long timeCounter = 0;


    /**
     * Stet up {@link #listPlayer} and set end event.
     * @param playlist contains {@link Song}
     * @param streamUrl address and port of stream
     */
    public StreamPlayer(final Playlist playlist, final StreamUrl streamUrl) {
        super(playlist);
        final MediaListRef mediaList = mediaPlayerFactory.media().newMediaListRef();
        listPlayer.list().setMediaList(mediaList);
        address = formatRtpStream(streamUrl.getMulticastAddress(), streamUrl.getPort());
        this.listPlayer.events().addMediaListPlayerEventListener(new MediaListPlayerEventAdapter() {
            @Override
            public void mediaListPlayerFinished(final MediaListPlayer listPlayer) {
                listPlayer.submit(() -> {
                    endTimeTracker();
                    listPlayer.list().media().clear();
                    new EndEvent().run();
                    currentSong = playlist.getCurrentSong();
                    listPlayer.list().media().add(currentSong.getFilepath(), address);
                    listPlayer.controls().play();
                    setUpTimeTracker();
                });
            }
        });
    }

    @Override
    public void startPlay() {
        currentSong = playlist.getCurrentSong();
        listPlayer.list().media().add(currentSong.getFilepath(), address);

        final Song song = playlist.getCurrentSong();
        support.firePropertyChange(NEW_SONG, null, song);
        support.firePropertyChange(Playlist.PLAYLIST_CHANGE, null, playlist);
        listPlayer.controls().play();
        support.firePropertyChange(START_PLAYER, !mediaPlayer.status().isPlaying(),
                mediaPlayer.status().isPlaying());

        setUpTimeTracker();
    }

    @Override
    public TimeBean createPlayTimeBean() {
        return new TimeBean(currentSong.getLength(), timeCounter);
    }

    private String formatRtpStream(final String multicastAddress, final String musicPort) {
        return ":sout=#rtp{dst="
                + multicastAddress
                + ",port="
                + musicPort
                + ",mux=ts}";
    }


    private void endTimeTracker() {
        timerTask.cancel();
        timeCounter = 0;
    }

    private void setUpTimeTracker() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                timeCounter++;
            }
        };
        timer.schedule(timerTask, 0, 1);
        timerTask.run();
    }
}
