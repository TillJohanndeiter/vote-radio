package de.techfak.gse.tjohanndeiter.model.database;

import de.techfak.gse.tjohanndeiter.model.exception.shutdown.VlcJException;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.waiter.media.ParsedWaiter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

public class SongFactory {


    public Song createSong(final File songFile) throws VlcJException {
        final MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        final Song song = createSong(songFile, mediaPlayerFactory);
        mediaPlayerFactory.release();
        return song;
    }


    Song createSong(final File songFile, final MediaPlayerFactory mediaPlayerFactory)
            throws VlcJException {

        final String absoluteFilePath = songFile.getAbsolutePath();
        final Media media = mediaPlayerFactory.media().newMedia(absoluteFilePath);
        final ParsedWaiter parsed = new ParsedWaiter(media) { //NOPMD
            @Override
            protected boolean onBefore(final Media component) {
                return media.parsing().parse();
            }
        };

        try {
            parsed.await();
        } catch (InterruptedException e) {
            throw new VlcJException("ParsedWaiter interrupted!", e);
        }

        final MetaData metaData = media.meta().asMetaData();
        final int length = (int) Duration.ofMillis(media.info().duration()).toSeconds();
        final Song song = createSong(songFile.getAbsolutePath(), length, metaData);
        media.release();
        return song;
    }

    /**
     * Creates {@link Song} instance.
     *
     * @param filepath filepath of .mp3 file
     * @param metaData parsed metaData of song
     * @param length   length of song in ms
     * @return song object with metadata
     */

    private Song createSong(final String filepath, final int length, final MetaData metaData) {
        final String title = metaData.get(Meta.TITLE);
        final String artist = metaData.get(Meta.ARTIST);
        final String album = metaData.get(Meta.ALBUM);
        final String genre = metaData.get(Meta.GENRE);
        String coverPath = metaData.get(Meta.ARTWORK_URL);
        byte[] cover = null;
        if (coverPath != null) {
            coverPath = coverPath.substring(7);
            coverPath = coverPath.replace("%20", " ");
            File coverFile = new File(coverPath);
            try {
                cover = Files.readAllBytes(coverFile.toPath().toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Song(filepath, title, artist, album, genre, length, cover);
    }
}