package de.tjohanndeiter.mode.server;

import de.tjohanndeiter.exception.client.UserDoesntExits;
import de.tjohanndeiter.exception.client.UserVotedAlreadyException;
import de.tjohanndeiter.exception.database.SongIdNotAvailable;
import de.tjohanndeiter.json.JsonException;
import de.tjohanndeiter.model.voting.VoteStrategy;
import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.IHTTPSession;
import static fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;


/**
 * Responsible for handling http rest requests form {@link de.tjohanndeiter.mode.client.Client}.
 * Use {@link ModelConnector} to connect to model classes.
 */
public class SessionHandler { //NOPMD

    public static final String CURRENT_SONG = "/current-song";
    public static final String PLAYLIST = "/playlist";
    public static final String UPLOAD_FILE = "/uploadFile";
    public static final String STREAMING_ADDRESS = "/streamingUrl";
    public static final String HANDSHAKE = "/handshake";
    public static final String USER = "/userInformation";
    public static final String RESPONSE_HANDSHAKE = "GSERadio!";
    public static final String VOTED_SONG_PARAM = "votedSong";
    private static final String JSON = "application/json";
    private static final String MIME_PLAINTEXT = NanoHTTPD.MIME_PLAINTEXT;
    private static final int FIRST_PARAM = 0;


    private final UploadRequester uploadRequester;
    private final VoteStrategy voteStrategy;
    private final ModelConnector modelConnector;

    /**
     * Constructor of session handler.
     *
     * @param voteStrategy    handel voting form server
     * @param uploadRequester handel writing of uploaded mp3 files form server
     * @param modelConnector  connection to model classes
     */
    SessionHandler(final VoteStrategy voteStrategy, final UploadRequester uploadRequester,
                   final ModelConnector modelConnector) {
        this.voteStrategy = voteStrategy;
        this.uploadRequester = uploadRequester;
        this.modelConnector = modelConnector;
    }

    /**
     * Handel http requests.
     *
     * @param session session of a requests.
     * @return response form server
     */
    Response serve(final IHTTPSession session) {
        session.getRemoteIpAddress();
        Response response = badRequestPlainText("Invalid Request"); //NOPMD
        final String request = session.getUri();
        switch (request) {
            case CURRENT_SONG:
                response = getCurrentSongResponse();
                break;
            case PLAYLIST:
                response = jsonOKResponse(modelConnector.getPlaylistJson());
                break;
            case UPLOAD_FILE:
                response = uploadRequester.handleFileUpload(session);
                break;
            case HANDSHAKE:
                response = jsonOKResponse(RESPONSE_HANDSHAKE);
                break;
            case STREAMING_ADDRESS:
                response = jsonOKResponse(modelConnector.getStreamingUrlJson());
                break;
            case USER:
                response = getUser(session);
                break;
            default:
                if (session.getParameters().get(VOTED_SONG_PARAM) != null) {
                    response = handleVoteForSong(session);
                }
                break;
        }
        return response;
    }

    private Response getCurrentSongResponse() {
        try {
            return jsonOKResponse(modelConnector.getCurrentSongJson());
        } catch (JsonException e) {
            return internalErrorResponse(e.getMessage());
        }
    }


    private Response getUser(final IHTTPSession session) {
        final String ip = session.getRemoteIpAddress();
        try {
            return jsonOKResponse(modelConnector.getJsonUser(ip));
        } catch (JsonException | UserDoesntExits e) {
            return internalErrorResponse(e.getMessage());
        }
    }

    private Response handleVoteForSong(final IHTTPSession session) {
        try {
            final int idToVote = Integer.parseInt(session.getParameters().get(VOTED_SONG_PARAM).get(FIRST_PARAM));
            final User user = modelConnector.getUser(session.getRemoteIpAddress());
            voteStrategy.voteById(idToVote, user);
            return plainTextOKResponse("Voted For Song" + idToVote);
        } catch (NumberFormatException n) {
            return badRequestPlainText("Invalid voteRequest");
        } catch (SongIdNotAvailable | UserVotedAlreadyException | UserDoesntExits e) {
            return badRequestPlainText(e.getMessage());
        }

    }

    private Response jsonOKResponse(final String json) {
        return newFixedLengthResponse(Response.Status.OK, JSON, json);
    }

    private Response plainTextOKResponse(final String text) {
        return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, text);
    }

    private Response internalErrorResponse(final String text) {
        return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, text);
    }

    private Response badRequestPlainText(final String text) {
        return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, text);
    }
}
