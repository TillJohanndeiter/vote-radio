package de.techfak.gse.tjohanndeiter.controller.cmd;

import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.server.RestServer;

public class ServerController extends TerminalController {


    private static final String PORT_CMD = "port";
    private static final String ADDRESS_CMD = "adress";


    private RestServer server;

    public ServerController(final Playlist playlist, final RestServer server) {
        super(playlist);
        this.server = server;

    }

    @Override
    void interpretUserInput(final String currentInput) {
        switch (currentInput) {
            case PORT_CMD:
                System.out.println(server.getPort()); //NOPMD
                break;
            case ADDRESS_CMD:
                System.out.println(server.getIpAddress()); //NOPMD
                break;
            default:
                super.interpretUserInput(currentInput);
                break;
        }
    }

    @Override
    void printHelp() {
        super.printHelp();
        System.out.println(PORT_CMD + " //Get current port"); //NOPMD
        System.out.println(ADDRESS_CMD + " //Get current address"); //NOPMD
    }

    @Override
    public void close() {
        super.close();
        server.stop();
    }
}
