package de.techfak.gse.tjohanndeiter.model.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.techfak.gse.tjohanndeiter.model.client.SongUpload;
import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.database.SongFactory;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;
import de.techfak.gse.tjohanndeiter.model.exception.database.SongAlreadyExitsException;
import de.techfak.gse.tjohanndeiter.model.exception.database.SongIdNotAvailable;
import de.techfak.gse.tjohanndeiter.model.exception.shutdown.VlcJException;
import de.techfak.gse.tjohanndeiter.model.json.QueueSongJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.QueueSongJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.json.SongUploadJSonParser;
import de.techfak.gse.tjohanndeiter.model.json.SongUploadParser;
import de.techfak.gse.tjohanndeiter.model.json.TimeBeanJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.TimeBeanJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.json.VoteListJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.VoteListJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import fi.iki.elonen.NanoHTTPD;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static fi.iki.elonen.NanoHTTPD.IHTTPSession;
import static fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;


public class SessionHandler implements PropertyChangeListener {

    public static final String CURRENT_SONG = "/current-song";
    public static final String PLAYLIST = "/playlist";
    public static final String UPLOAD_FILE = "/uploadFile";
    public static final String STREAMING_ADDRESS = "/streamingAddress";
    public static final String STREAMING_PORT = "/streamingPort";
    public static final String HANDSHAKE = "/handshake";
    public static final String PLAY_TIME = "/playTime";
    public static final String RESPONSE_HANDSHAKE = "GSERadio!";
    public static final String VOTED_SONG_PARAM = "votedSong";

    private static final String MIME_PLAINTEXT = NanoHTTPD.MIME_PLAINTEXT;
    private static final int FIRST_PARAM = 0;

    private QueueSongJsonParser songJsonParser = new QueueSongJsonParserImpl();
    private VoteListJsonParser voteListJsonParser = new VoteListJsonParserImpl();
    private SongUploadJSonParser songUploadJSonParser = new SongUploadParser();
    private TimeBeanJsonParser timeBeanJsonParser = new TimeBeanJsonParserImpl();
    private SongFactory songFactory = new SongFactory();

    private String addressStream;
    private String playlistJson;
    private String currentSongJson;

    private int portStream;
    private VoteList voteList;
    private SongLibrary songLibrary;
    private MusicPlayer musicPlayer;

    public SessionHandler(final String addressStream, final int portStream,
                          final SongLibrary songLibrary, final VoteList voteList, final MusicPlayer musicPlayer) {
        this.addressStream = addressStream;
        this.portStream = portStream;
        this.songLibrary = songLibrary;
        this.voteList = voteList;
        this.musicPlayer = musicPlayer;
        voteList.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent propertyChangeEvent) {
        try {
            switch (propertyChangeEvent.getPropertyName()) { //NOPMD
                case Playlist.PLAYLIST_CHANGE:
                case VoteList.VOTE_CHANGED:
                    refreshPlaylist(propertyChangeEvent);
                    break;
                case Playlist.NEW_SONG:
                    currentSongJson = songJsonParser.toJson((QueueSong) propertyChangeEvent.getNewValue());
                    break;
                default:
                    break;
            }
        } catch (JsonProcessingException e) {
            System.out.println("Warning: Json Parsing failed!"); //NOPMD
        }
    }


    Response serve(final IHTTPSession session) {
        Response response = newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid Request");
        final String request = session.getUri();
        switch (request) {
            case CURRENT_SONG:
                response = getCurrentSongResponse();
                break;
            case PLAYLIST:
                response = getPlaylistResponse();
                break;
            case UPLOAD_FILE:
                response = handleFileUpload(session);
                break;
            case STREAMING_ADDRESS:
                response = getStreamingAddress();
                break;
            case STREAMING_PORT:
                response = getPortStream();
                break;
            case HANDSHAKE:
                response = newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, RESPONSE_HANDSHAKE);
                break;
            case PLAY_TIME:
                response = getTimeBean();
                break;
            default:
                if (session.getParameters().get(VOTED_SONG_PARAM) != null) {
                    response = handleVoteForSong(session);
                }
                break;
        }
        return response;
    }

    private Response getTimeBean() {
        try {
            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT,
                    timeBeanJsonParser.toJson(musicPlayer.createPlayTimeBean()));
        } catch (JsonProcessingException e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Json failed");
        }
    }

    private Response getPortStream() {
        return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, Integer.toString(portStream));
    }

    private Response getStreamingAddress() {
        return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, addressStream);
    }

    private Response handleFileUpload(final IHTTPSession session) {
        try {
            final String postData = getPostBody(session);
            final SongUpload songUpload = songUploadJSonParser.toSongUpload(postData);
            final File fileSong = new File(songLibrary.getAbsoluteFilepath() + '/' + songUpload.getName());

            if (checkIfAlreadyExists(fileSong)) {
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT,
                        songUpload.getName() + "already exits");
            }

            try (final FileOutputStream fileOutputStream = new FileOutputStream(fileSong)) {
                final byte[] decode = Base64.getDecoder().decode(songUpload.getBase64Code());
                fileOutputStream.write(decode);
            }
            addSongToLibraryAndPlaylist(fileSong);

            return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, "Uploaded :" + songUpload.getName());
        } catch (IOException | NanoHTTPD.ResponseException | VlcJException e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "SongUpload Failed!");
        } catch (SongAlreadyExitsException e) {
            return newFixedLengthResponse(Response.Status.NOT_ACCEPTABLE, MIME_PLAINTEXT, "Song already Exits");
        }
    }

    private void addSongToLibraryAndPlaylist(final File fileSong) throws VlcJException, SongAlreadyExitsException {
        final Song song = songFactory.createSong(fileSong);
        songLibrary.addSong(song);
        voteList.addSong(song);
    }

    private boolean checkIfAlreadyExists(final File fileSong) {
        return fileSong.exists();
    }

    private String getPostBody(final IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
        final ConcurrentMap<String, String> body = new ConcurrentHashMap<>();
        session.parseBody(body);
        return body.get("postData");
    }

    private Response handleVoteForSong(final NanoHTTPD.IHTTPSession session) {
        Response response;
        try {
            final int idToVote = Integer.parseInt(session.getParameters().get(VOTED_SONG_PARAM).get(FIRST_PARAM));
            voteList.voteForSongById(idToVote);
            response = newFixedLengthResponse(
                    NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, "Voted For Song" + idToVote);
        } catch (NumberFormatException n) {
            response = newFixedLengthResponse(
                    NanoHTTPD.Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid voteRequest");
        } catch (SongIdNotAvailable e) {
            response = newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid Song id");
        }

        return response;
    }

    private Response getPlaylistResponse() {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, playlistJson);
    }

    private NanoHTTPD.Response getCurrentSongResponse() {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, currentSongJson);
    }

    private void refreshPlaylist(final PropertyChangeEvent propertyChangeEvent) throws JsonProcessingException {
        playlistJson = voteListJsonParser.toJson((VoteList) propertyChangeEvent.getNewValue());
    }

    public VoteList getVoteList() {
        return voteList;
    }
}
