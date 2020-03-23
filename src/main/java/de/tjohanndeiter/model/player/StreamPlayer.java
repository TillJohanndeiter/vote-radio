package de.tjohanndeiter.model.player;

import de.tjohanndeiter.exception.shutdown.InvalidPortException;
import de.tjohanndeiter.exception.shutdown.MusicStreamPortInUseException;
import de.tjohanndeiter.model.database.Song;
import de.tjohanndeiter.model.playlist.Playlist;
import de.tjohanndeiter.mode.server.StreamUrl;
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Timer;
import java.util.TimerTask;

public class StreamPlayer extends MusicPlayer {

    private static final int TIMEOUT = 1000;
    private static final int BUFFER = 2048;

    private final String address;
    private final MediaListPlayer listPlayer = mediaPlayerFactory.mediaPlayers().newMediaListPlayer();
    private final Timer timer = new Timer();
    private Song currentSong;
    private TimerTask timerTask;
    private long timeCounter = 0;


    /**
     * Stet up {@link #listPlayer} and set end event.
     *
     * @param playlist  contains {@link Song}
     * @param streamUrl address and port of stream
     */
    public StreamPlayer(final Playlist playlist, final StreamUrl streamUrl) throws InvalidPortException,
            MusicStreamPortInUseException {
        super(playlist);
        address = formatRtpStream(streamUrl.getMulticastAddress(), streamUrl.getPort());
        if (portUsed(Integer.parseInt(streamUrl.getPort()))) {
            throw new MusicStreamPortInUseException(streamUrl.getMulticastAddress(), streamUrl.getPort());
        }
        final MediaListRef mediaList = mediaPlayerFactory.media().newMediaListRef();
        setUpEndEvent(playlist, mediaList);
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

    private boolean portUsed(final int port) throws InvalidPortException {


        boolean tcpOnPort;

        //TCP
        try (final ServerSocket serverSocket = new ServerSocket(port)) {
            tcpOnPort = serverSocket.isClosed();
        } catch (IllegalArgumentException e) {
            throw new InvalidPortException(port, e);
        } catch (IOException e) {
            tcpOnPort = true;
        }

        boolean udpOnPort;

        //UDP
        try (final DatagramSocket server = new DatagramSocket(port)) {
            server.setSoTimeout(TIMEOUT);
            final byte[] data = new byte[BUFFER];
            final DatagramPacket packet = new DatagramPacket(data, 0, data.length);
            server.receive(packet);
            udpOnPort = true;
        } catch (IOException e) {
            udpOnPort = false;
        }

        return (udpOnPort || tcpOnPort); //NOPMD
    }


    private String formatRtpStream(final String multicastAddress, final String musicPort) {
        return ":sout=#rtp{dst="
                + multicastAddress
                + ",port="
                + musicPort
                + ",mux=ts}";
    }

    private void setUpEndEvent(final Playlist playlist, final MediaListRef mediaList) {
        listPlayer.list().setMediaList(mediaList);
        this.listPlayer.events().addMediaListPlayerEventListener(new MediaListPlayerEventAdapter() {
            @Override
            public void mediaListPlayerFinished(final MediaListPlayer listPlayer) {
                listPlayer.submit(() -> {
                    endTimeTracker();
                    listPlayer.list().media().clear();
                    playlist.skipToNext();
                    final Song song = playlist.getCurrentSong();
                    support.firePropertyChange(NEW_SONG, null, song);
                    currentSong = playlist.getCurrentSong();
                    listPlayer.list().media().add(currentSong.getFilepath(), address);
                    listPlayer.controls().play();
                    setUpTimeTracker();
                });
            }
        });
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
