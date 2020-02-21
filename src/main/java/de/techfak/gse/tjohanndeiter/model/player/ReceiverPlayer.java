package de.techfak.gse.tjohanndeiter.model.player;

public class ReceiverPlayer extends MusicPlayer {

    private String completeAdress;

    public ReceiverPlayer(final String serverAdress, final String port) {
        super(null);
        completeAdress = "rtp://@" + serverAdress + ':' + port;
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
        propertyChangeSupport.firePropertyChange(START_PLAYER, !mediaPlayer.status().isPlaying(),
                mediaPlayer.status().isPlaying());
        mediaPlayer.submit(() -> mediaPlayer.media().play(completeAdress));
    }
}
