package de.tjohanndeiter.model.player;

import de.tjohanndeiter.mode.server.StreamUrl;

public class ReceiverPlayer extends MusicPlayer {

    private final String rtpAddress;

    public ReceiverPlayer(final StreamUrl streamUrl) {
        super(null);
        rtpAddress = "rtp://@" + streamUrl.getMulticastAddress() + ':' + streamUrl.getPort();
    }

    @Override
    public void changePlayingState() {
        if (mediaPlayer.status().isPlaying()) {
            stop();
        } else {
            startPlay();
        }
    }

    @Override
    public void startPlay() {
        support.firePropertyChange(START_PLAYER, !mediaPlayer.status().isPlaying(),
                mediaPlayer.status().isPlaying());
        mediaPlayer.submit(() -> mediaPlayer.media().play(rtpAddress));
    }
}
