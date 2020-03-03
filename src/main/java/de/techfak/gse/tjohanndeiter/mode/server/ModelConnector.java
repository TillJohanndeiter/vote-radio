package de.techfak.gse.tjohanndeiter.mode.server;

import de.techfak.gse.tjohanndeiter.exception.client.UserDoesntExits;
import de.techfak.gse.tjohanndeiter.json.JsonException;
import de.techfak.gse.tjohanndeiter.json.QueueSongJsonParser;
import de.techfak.gse.tjohanndeiter.json.QueueSongJsonParserImpl;
import de.techfak.gse.tjohanndeiter.json.TimeBeanJsonParser;
import de.techfak.gse.tjohanndeiter.json.TimeBeanJsonParserImpl;
import de.techfak.gse.tjohanndeiter.json.UserJsonParser;
import de.techfak.gse.tjohanndeiter.json.UserJsonParserImpl;
import de.techfak.gse.tjohanndeiter.json.VoteListJsonParser;
import de.techfak.gse.tjohanndeiter.json.VoteListJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * Responsible for Connection between server layer especially {@link SessionHandler} and model. Communicate with model
 * via observer structure for {@link #currentSongJson} and {@link #playlistJson} and directly
 * {@link #getJsonPlayTimeBean()}.
 */
class ModelConnector implements PropertyChangeListener {

    private final QueueSongJsonParser songJsonParser = new QueueSongJsonParserImpl();
    private final VoteListJsonParser voteListJsonParser = new VoteListJsonParserImpl();
    private final TimeBeanJsonParser timeBeanJsonParser = new TimeBeanJsonParserImpl();
    private final UserJsonParser userJsonParser = new UserJsonParserImpl();

    private MusicPlayer musicPlayer;

    private String playlistJson;
    private String currentSongJson;
    private UserManger userManger;

    ModelConnector(final MusicPlayer musicPlayer, final UserManger userManger) {
        this.musicPlayer = musicPlayer;
        this.userManger = userManger;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent propertyChangeEvent) {
        try {
            switch (propertyChangeEvent.getPropertyName()) { //NOPMD
                case Playlist.PLAYLIST_CHANGE:
                    refreshPlaylistJson(propertyChangeEvent);
                    break;
                case MusicPlayer.NEW_SONG:
                    refreshSongJson(propertyChangeEvent);
                    break;
                default:
                    break;
            }
        } catch (JsonException e) {
            e.printStackTrace();
        }
    }

    String getJsonPlayTimeBean() throws JsonException {
        return timeBeanJsonParser.toJson(musicPlayer.createPlayTimeBean());
    }

    String getJsonUser(final String ipAddress) throws UserDoesntExits, JsonException {
        return userJsonParser.toJson(userManger.getUserByIp(ipAddress));
    }

    User getUser(final String ipAddress) throws UserDoesntExits {
        return userManger.getUserByIp(ipAddress);
    }

    private void refreshSongJson(final PropertyChangeEvent propertyChangeEvent) throws JsonException {
        currentSongJson = songJsonParser.toJson((QueueSong) propertyChangeEvent.getNewValue());
    }

    private void refreshPlaylistJson(final PropertyChangeEvent propertyChangeEvent) throws JsonException {
        playlistJson = voteListJsonParser.toJson((VoteList) propertyChangeEvent.getNewValue());
    }

    String getPlaylistJson() {
        return playlistJson;
    }

    String getCurrentSongJson() {
        return currentSongJson;
    }
}
