package de.techfak.gse.tjohanndeiter.model.database;

import de.techfak.gse.tjohanndeiter.exception.database.SongAlreadyExitsException;
import de.techfak.gse.tjohanndeiter.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.exception.shutdown.NoMp3FilesException;
import de.techfak.gse.tjohanndeiter.exception.shutdown.VlcJException;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;

import java.io.File;
import java.util.Objects;

/**
 * Creates {@link SongLibrary} with *.mp3 files from a folder. Use VlcJ library for implementation. For creation
 * of {@link Song} bean {@link SongFactory} is used.
 */
public class SongLibraryVlcJFactory implements SongLibraryFactory {


    private final SongFactory songFactory = new SongFactory();

    /**
     * Creates {@link SongLibrary}. Parse als .mp3 files form the folder with
     * metadata and creates {@linkplain SongLibrary} with {@linkplain Song} objects.
     *
     * @return {@linkplain SongLibrary} with song objects
     * @throws NoMp3FilesException if no mp3 files in folder
     */

    @Override
    public SongLibrary createSongLibrary(final File path) throws ShutdownException {
        final String filepath = path.getAbsolutePath();
        final SongLibrary songLibrary = new SongLibraryImpl(filepath);

        final File folder = new File(filepath);


        if (!folder.isDirectory()) {
            throw new NoMp3FilesException(filepath + " Is not a Folder");
        }


        final File[] mp3FilesInFolder = folder.listFiles((File file, String s) -> s.endsWith(".mp3"));

        if (Objects.requireNonNull(mp3FilesInFolder).length == 0) {
            throw new NoMp3FilesException(filepath + " Contain not valid mp3 Files");
        }

        addMp3inFolderToLibrary(songLibrary, mp3FilesInFolder);

        return songLibrary;
    }

    /**
     * Add mp3 files in a folder to the library.
     *
     * @param songLibrary      library to create.
     * @param mp3FilesInFolder all mp3 folders to include in library.
     */
    private void addMp3inFolderToLibrary(final SongLibrary songLibrary, final File... mp3FilesInFolder)
            throws VlcJException {
        final MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        for (final File file : mp3FilesInFolder) {
            try {
                songLibrary.addSong(songFactory.createSong(file, mediaPlayerFactory));
            } catch (SongAlreadyExitsException ignored) {
            }
        }

        mediaPlayerFactory.release();
    }


}
