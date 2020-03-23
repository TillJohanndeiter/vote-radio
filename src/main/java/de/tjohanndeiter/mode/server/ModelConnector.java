package de.tjohanndeiter.mode.server;

import de.tjohanndeiter.exception.client.UserDoesntExits;
import de.tjohanndeiter.exception.shutdown.RestServerException;
import de.tjohanndeiter.json.JsonException;
import de.tjohanndeiter.json.QueueSongJsonParser;
import de.tjohanndeiter.json.QueueSongJsonParserImpl;
import de.tjohanndeiter.json.StreamUrlJsonParser;
import de.tjohanndeiter.json.StreamUrlJsonParserImpl;
import de.tjohanndeiter.json.TimeBeanJsonParser;
import de.tjohanndeiter.json.TimeBeanJsonParserImpl;
import de.tjohanndeiter.json.UserJsonParser;
import de.tjohanndeiter.json.UserJsonParserImpl;
import de.tjohanndeiter.json.VoteListJsonParser;
import de.tjohanndeiter.json.VoteListJsonParserImpl;
import de.tjohanndeiter.model.player.MusicPlayer;
import de.tjohanndeiter.model.playlist.Playlist;
import de.tjohanndeiter.model.playlist.VotedSong;
import de.tjohanndeiter.model.playlist.VoteList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * Responsible for Connection between server layer especially {@link SessionHandler} and model. Communicate with model
 * via observer structure for {@link #currentSongJson} and {@link #playlistJson} and directly
 * {@link #getTimeBean()} ()}.
 */
class ModelConnector implements PropertyChangeListener {

    private final QueueSongJsonParser songJsonParser = new QueueSongJsonParserImpl();
    private final VoteListJsonParser voteListJsonParser = new VoteListJsonParserImpl();
    private final TimeBeanJsonParser timeBeanJsonParser = new TimeBeanJsonParserImpl();
    private final UserJsonParser userJsonParser = new UserJsonParserImpl();

    private MusicPlayer musicPlayer;
    private UserManger userManger;

    private String playlistJson;
    private String currentSongJson;
    private String streamUrlJson;

    ModelConnector(final MusicPlayer musicPlayer, final UserManger userManger, final StreamUrl streamUrl)
            throws RestServerException {
        this.musicPlayer = musicPlayer;
        this.userManger = userManger;
        final StreamUrlJsonParser streamAddressJsonParser = new StreamUrlJsonParserImpl();
        initStreamUrlJson(streamUrl, streamAddressJsonParser);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent changeEvent) {
        try {
            switch (changeEvent.getPropertyName()) { //NOPMD
                case Playlist.PLAYLIST_CHANGE:
                    refreshPlaylistJson(changeEvent);
                    break;
                case MusicPlayer.NEW_SONG:
                    refreshSongJson(changeEvent);
                    break;
                default:
                    break;
            }
        } catch (JsonException e) {
            e.printStackTrace();
        }
    }

    String getJsonUser(final String ipAddress) throws UserDoesntExits, JsonException {
        return userJsonParser.toJson(userManger.getUserByIp(ipAddress));
    }

    User getUser(final String ipAddress) throws UserDoesntExits {
        return userManger.getUserByIp(ipAddress);
    }

    String getCurrentSongJson() throws JsonException {
        return "{\"queueSong\":" + currentSongJson + ",\"timeBean\":" + getTimeBean() + '}';
    }

    String getTimeBean() throws JsonException {
        return timeBeanJsonParser.toJson(musicPlayer.createPlayTimeBean());
    }

    String getPlaylistJson() {
        return playlistJson;
    }

    String getStreamingUrlJson() {
        return streamUrlJson;
    }

    private void refreshSongJson(final PropertyChangeEvent propertyChangeEvent) throws JsonException {
        currentSongJson = songJsonParser.toJson((VotedSong) propertyChangeEvent.getNewValue());
    }

    private void refreshPlaylistJson(final PropertyChangeEvent propertyChangeEvent) throws JsonException {
        playlistJson = voteListJsonParser.toJson((VoteList) propertyChangeEvent.getNewValue());
    }

    private void initStreamUrlJson(final StreamUrl streamUrl, final StreamUrlJsonParser streamAddressJsonParser)
            throws RestServerException {
        try {
            this.streamUrlJson = streamAddressJsonParser.toJson(streamUrl);
        } catch (JsonException e) {
            throw new RestServerException("Falied to serialize music stream bean", e);
        }
    }

}
