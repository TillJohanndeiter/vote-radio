package de.techfak.gse.tjohanndeiter.model.player;

public class ReceiverPlayer extends MusicPlayer {

    private String completeAdress;

    public ReceiverPlayer(final String serverAdress, final String port) {
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
        super.startPlay();
        mediaPlayer.submit(() -> mediaPlayer.media().play(completeAdress));
    }
}
