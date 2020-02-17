package de.techfak.gse.tjohanndeiter.model.player;

import de.techfak.gse.tjohanndeiter.model.exception.prototypes.StartPlayerException;
import de.techfak.gse.tjohanndeiter.model.exception.shutdown.InvalidPortException;
import de.techfak.gse.tjohanndeiter.model.exception.shutdown.MusicStreamPortInUseException;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;


@Deprecated
public class OldStreamPlayer extends MusicPlayer {

    private static final int TIMEOUT = 1000;
    private static final int BUFFER = 2048;
    private Playlist playlist;
    private String streamAddress;

    public OldStreamPlayer(final Playlist playlist, final String multicastAddress, final int port)
            throws StartPlayerException {
        this.playlist = playlist;
        if (portUsed(port)) {
            throw new MusicStreamPortInUseException(multicastAddress, port);
        }
        streamAddress = formatRtpStream(multicastAddress, port);
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                mediaPlayer.submit(() -> mediaPlayer.media().play(playlist.getNextSong().getFilepath(), streamAddress));
            }
        });
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

        return (udpOnPort || tcpOnPort);
    }

    @Override
    public void startPlay() {
        mediaPlayer.media().play(playlist.getCurrentSong().getFilepath(), streamAddress);
    }

    private String formatRtpStream(final String multicastAddress, final int musicPort) {
        return ":sout=#rtp{dst="
                + multicastAddress
                + ",port="
                + musicPort
                + ",mux=ts}";
    }
}
