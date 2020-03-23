package de.tjohanndeiter.mode.server;

import de.tjohanndeiter.json.JsonException;
import de.tjohanndeiter.mode.client.SongUpload;
import de.tjohanndeiter.model.database.Song;
import de.tjohanndeiter.model.database.SongFactory;
import de.tjohanndeiter.model.database.SongLibrary;
import de.tjohanndeiter.exception.database.SongAlreadyExitsException;
import de.tjohanndeiter.exception.shutdown.VlcJException;
import de.tjohanndeiter.json.SongUploadJSonParser;
import de.tjohanndeiter.json.SongUploadParser;
import de.tjohanndeiter.model.playlist.VoteList;
import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

/**
 * Requester for mp3 uploads form client.
 */
class UploadRequester {

    private final SongFactory songFactory = new SongFactory();
    private final SongUploadJSonParser songUploadJSonParser = new SongUploadParser();
    private final SongLibrary songLibrary;
    private final VoteList voteList;

    UploadRequester(final SongLibrary songLibrary, final VoteList voteList) {
        this.songLibrary = songLibrary;
        this.voteList = voteList;
    }


    /**
     * Parse base64 string from request body. Checks if file does exits. Write file to music folder of
     * {@link #songLibrary} and add Song to {@link #voteList}
     * @param session upload session
     * @return response contains about success of upload
     */
    NanoHTTPD.Response handleFileUpload(final NanoHTTPD.IHTTPSession session) {
        try {
            final String postData = getPostBody(session);
            final SongUpload songUpload = songUploadJSonParser.toSongUpload(postData);
            final File fileSong = new File(songLibrary.getAbsoluteFilepath() + '/' + songUpload.getFileName());

            if (fileSong.exists()) {
                return internalErrorResponse(songUpload.getFileName() + "already exits");
            }

            writeToMusicFolder(songUpload, fileSong);
            addSongToLibraryAndPlaylist(fileSong);

            return plainTextOKResponse("Uploaded :" + songUpload.getFileName());
        } catch (IOException | NanoHTTPD.ResponseException | VlcJException | JsonException e) {
            return internalErrorResponse("SongUpload Failed!");
        } catch (SongAlreadyExitsException e) {
            return internalErrorResponse("Song already Exits");
        }
    }

    private void writeToMusicFolder(final SongUpload songUpload, final File fileSong) throws IOException {
        try (final FileOutputStream fileOutputStream = new FileOutputStream(fileSong)) {
            final byte[] decode = Base64.getDecoder().decode(songUpload.getBase64Code());
            fileOutputStream.write(decode);
        }
    }


    private void addSongToLibraryAndPlaylist(final File fileSong) throws VlcJException, SongAlreadyExitsException {
        final Song song = songFactory.createSong(fileSong);
        songLibrary.addSong(song);
        voteList.addSong(song);
    }

    private String getPostBody(final NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
        final ConcurrentMap<String, String> body = new ConcurrentHashMap<>();
        session.parseBody(body);
        return body.get("postData");
    }

    private NanoHTTPD.Response plainTextOKResponse(final String text) {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, text);
    }

    private NanoHTTPD.Response internalErrorResponse(final String text) {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, text);
    }
}
