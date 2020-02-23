package de.techfak.gse.tjohanndeiter.model.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.techfak.gse.tjohanndeiter.model.json.QueueSongJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.QueueSongJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.json.VoteListJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.VoteListJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.player.TimeBean;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import fi.iki.elonen.NanoHTTPD;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;


public class ModelConnector implements PropertyChangeListener {

    private QueueSongJsonParser songJsonParser = new QueueSongJsonParserImpl();
    private VoteListJsonParser voteListJsonParser = new VoteListJsonParserImpl();

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

    TimeBean createPlayTimeBean() {
        return musicPlayer.createPlayTimeBean();
    }

    private void refreshSongJson(final PropertyChangeEvent propertyChangeEvent) throws JsonProcessingException {
        currentSongJson = songJsonParser.toJson((QueueSong) propertyChangeEvent.getNewValue());
    }

    private void refreshPlaylistJson(final PropertyChangeEvent propertyChangeEvent) throws JsonProcessingException {
        playlistJson = voteListJsonParser.toJson((VoteList) propertyChangeEvent.getNewValue());
    }

    NanoHTTPD.Response getPlaylistResponse() {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, playlistJson);
    }

    NanoHTTPD.Response getCurrentSongResponse() {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, currentSongJson);
    }

    public UserManger getUserManger() {
        return userManger;
    }
}
