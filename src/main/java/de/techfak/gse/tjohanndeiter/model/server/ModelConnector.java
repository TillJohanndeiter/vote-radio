package de.techfak.gse.tjohanndeiter.model.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.techfak.gse.tjohanndeiter.model.exception.client.UserDoesntExits;
import de.techfak.gse.tjohanndeiter.model.json.QueueSongJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.QueueSongJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.json.TimeBeanJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.TimeBeanJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.json.UserJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.UserJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.json.VoteListJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.VoteListJsonParserImpl;
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
public class ModelConnector implements PropertyChangeListener {

    private static final String JSON = "application/json";

    private QueueSongJsonParser songJsonParser = new QueueSongJsonParserImpl();
    private VoteListJsonParser voteListJsonParser = new VoteListJsonParserImpl();
    private TimeBeanJsonParser timeBeanJsonParser = new TimeBeanJsonParserImpl();
    private UserJsonParser userJsonParser = new UserJsonParserImpl();

    private MusicPlayer musicPlayer;

    private String playlistJson;
    private String currentSongJson;
    private UserManger userManger;

    public ModelConnector(final MusicPlayer musicPlayer, final UserManger userManger) {
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
        } catch (JsonProcessingException e) {
            System.out.println("Warning: Json Parsing failed!"); //NOPMD
        }
    }

    String getJsonPlayTimeBean() throws JsonProcessingException {
        return timeBeanJsonParser.toJson(musicPlayer.createPlayTimeBean());
    }

    String getJsonUser(final String ipAddress) throws UserDoesntExits, JsonProcessingException {
        return userJsonParser.toJson(userManger.getUserByIp(ipAddress));
    }

    User getUser(final String ipAddress) throws UserDoesntExits {
        return userManger.getUserByIp(ipAddress);
    }

    private void refreshSongJson(final PropertyChangeEvent propertyChangeEvent) throws JsonProcessingException {
        currentSongJson = songJsonParser.toJson((QueueSong) propertyChangeEvent.getNewValue());
    }

    private void refreshPlaylistJson(final PropertyChangeEvent propertyChangeEvent) throws JsonProcessingException {
        playlistJson = voteListJsonParser.toJson((VoteList) propertyChangeEvent.getNewValue());
    }

    String getPlaylistJson() {
        return playlistJson;
    }

    String getCurrentSongJson() {
        return currentSongJson;
    }
}
