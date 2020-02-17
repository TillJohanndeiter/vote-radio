package de.techfak.gse.tjohanndeiter.controller.cmd;

import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;

import java.util.Scanner;

public class TerminalController implements AutoCloseable {


    private static final String EXIT_CMD = "exit";
    private static final String SONG_INF_CMD = "song";
    private static final String PLAYLIST_CMD = "playlist";
    private static final String HELP_CMD = "help";

    private final Scanner scanner = new Scanner(System.in);
    private final Playlist playlist;

    public TerminalController(final Playlist playlist) {
        this.playlist = playlist;
    }


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
        switch (currentInput) {//NOPMD
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
        System.out.println(playlist.toString()); //NOPMD
    }

    private void printSongInfo() {
        //System.out.println(playlist.getCurrentSong().toString()); //NOPMD
    }

}
