package de.techfak.gse.tjohanndeiter.model.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.techfak.gse.tjohanndeiter.model.exception.client.UserDoesntExits;
import de.techfak.gse.tjohanndeiter.model.exception.client.UserVotedAlreadyException;
import de.techfak.gse.tjohanndeiter.model.exception.database.SongIdNotAvailable;
import de.techfak.gse.tjohanndeiter.model.json.TimeBeanJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.TimeBeanJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.json.UserJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.UserJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.voting.VoteStrategy;
import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.IHTTPSession;
import static fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;


public class SessionHandler {

    public static final String CURRENT_SONG = "/current-song";
    public static final String PLAYLIST = "/playlist";
    public static final String UPLOAD_FILE = "/uploadFile";
    public static final String STREAMING_ADDRESS = "/streamingAddress";
    public static final String STREAMING_PORT = "/streamingPort";
    public static final String HANDSHAKE = "/handshake";
    public static final String PLAY_TIME = "/playTime";
    public static final String USER = "/userInformation";
    public static final String RESPONSE_HANDSHAKE = "GSERadio!";
    public static final String VOTED_SONG_PARAM = "votedSong";

    private static final String MIME_PLAINTEXT = NanoHTTPD.MIME_PLAINTEXT;
    private static final int FIRST_PARAM = 0;


    private TimeBeanJsonParser timeBeanJsonParser = new TimeBeanJsonParserImpl();
    private UserJsonParser userJsonParser = new UserJsonParserImpl();
    private UploadRequester uploadRequester;


    private VoteStrategy voteStrategy;
    private StreamUrl portBean;
    private ModelConnector modelConnector;
    private UserManger userManger;

    public SessionHandler(final StreamUrl portBean, final VoteStrategy voteStrategy,
                          final UploadRequester uploadRequester, final ModelConnector modelConnector) {
        this.portBean = portBean;
        this.voteStrategy = voteStrategy;
        this.uploadRequester = uploadRequester;
        this.modelConnector = modelConnector;
        this.userManger = modelConnector.getUserManger();
    }


    Response serve(final IHTTPSession session) {
        session.getRemoteIpAddress();
        Response response = newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid Request");
        final String request = session.getUri();
        switch (request) {
            case CURRENT_SONG:
                response = modelConnector.getCurrentSongResponse();
                break;
            case PLAYLIST:
                response = modelConnector.getPlaylistResponse();
                break;
            case UPLOAD_FILE:
                response = uploadRequester.handleFileUpload(session);
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

    private Response getUser(final IHTTPSession session) {
        String ip = session.getRemoteIpAddress();
        try {
            return newFixedLengthResponse(Response.Status.OK,"application/json", userJsonParser.toJson(userManger.getUserByIp(ip)));
        } catch (JsonProcessingException | UserDoesntExits e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, e.getMessage());
        }
    }

    private Response getTimeBean() {
        try {
            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT,
                    timeBeanJsonParser.toJson(modelConnector.createPlayTimeBean()));
        } catch (JsonProcessingException e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, e.getMessage());
        }
    }

    private Response getPortStream() {
        return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, portBean.getPort());
    }

    private Response getStreamingAddress() {
        return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, portBean.getMulticastAddress());
    }

    private Response handleVoteForSong(final NanoHTTPD.IHTTPSession session) {
        Response response;
        try {
            final int idToVote = Integer.parseInt(session.getParameters().get(VOTED_SONG_PARAM).get(FIRST_PARAM));
            User user = userManger.getUserByIp(session.getRemoteIpAddress());
            voteStrategy.voteById(idToVote, user);
            response = newFixedLengthResponse(
                    NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, "Voted For Song" + idToVote);
        } catch (NumberFormatException n) {
            response = newFixedLengthResponse(
                    NanoHTTPD.Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid voteRequest");
        } catch (SongIdNotAvailable e) {
            response = newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT,
                    "Invalid Song id");
        } catch (UserVotedAlreadyException | UserDoesntExits e) {
            response = newFixedLengthResponse(Response.Status.FORBIDDEN, MIME_PLAINTEXT, e.getMessage());
        }

        return response;
    }
}
