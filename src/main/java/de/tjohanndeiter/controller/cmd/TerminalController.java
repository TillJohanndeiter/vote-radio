package de.tjohanndeiter.controller.cmd;

import de.tjohanndeiter.model.player.MusicPlayer;
import de.tjohanndeiter.model.playlist.Playlist;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Scanner;


/**
 * Simple controller for cmd interaction in random mode. Currently you can get information about current playlist, song
 * and a man page for information.
 */
public class TerminalController implements AutoCloseable, PropertyChangeListener {


    private static final String EXIT_CMD = "exit";
    private static final String SONG_INF_CMD = "song";
    private static final String PLAYLIST_CMD = "playlist";
    private static final String HELP_CMD = "help";

    private final Scanner scanner = new Scanner(System.in);
    private String currentPlaylist;
    private String currentSong;

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case MusicPlayer.NEW_SONG:
                currentSong = event.getNewValue().toString();
                break;
            case Playlist.PLAYLIST_CHANGE:
                currentPlaylist = event.getNewValue().toString();
                break;
            default:
                break;
        }
    }


    /**
     * Starts the input Loop with commands form the user.
     */
    public void inputLoop() {
        printHelp();

        boolean loopActive = true;

        while (loopActive) {
            final String currentInput = scanner.nextLine();
            if (currentInput.equals(EXIT_CMD)) {
                loopActive = false;
            } else {
                interpretUserInput(currentInput);
            }
        }

        close();
    }

    void interpretUserInput(final String currentInput) {
        switch (currentInput) { //NOPMD
            case SONG_INF_CMD:
                printSongInfo();
                break;
            case PLAYLIST_CMD:
                printPlayList();
                break;
            default:
                printHelp();
        }
    }

    void printHelp() {
        System.out.println("Welcome to GSE Radio: "); //NOPMD
        System.out.println("Commands: "); //NOPMD
        System.out.println(SONG_INF_CMD + " //Meta-Data form current song"); //NOPMD
        System.out.println(PLAYLIST_CMD + " // Print current playlist"); //NOPMD
        System.out.println(HELP_CMD + " //Print this help menu"); //NOPMD
    }

    @Override
    public void close() {
        scanner.close();
    }

    private void printPlayList() {
        System.out.println(currentPlaylist); //NOPMD
    }

    private void printSongInfo() {
        System.out.println(currentSong); //NOPMD
    }
}
