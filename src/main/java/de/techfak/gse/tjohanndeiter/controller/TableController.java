package de.techfak.gse.tjohanndeiter.controller;

import de.techfak.gse.tjohanndeiter.model.client.Client;
import de.techfak.gse.tjohanndeiter.model.exception.client.UserVotedAlreadyException;
import de.techfak.gse.tjohanndeiter.model.exception.database.SongIdNotAvailable;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.server.User;
import de.techfak.gse.tjohanndeiter.model.voting.VoteStrategy;
import de.techfak.gse.tjohanndeiter.view.ActionButtonTableCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;


/**
 * Responsible for update playlist including metadata, votes and needed replay for each {@link QueueSong} in playlist.
 * Also send a vote to {@link #voteStrategy} if user give a vote by click on #ActionButtonTableCell in
 * {@link #buttonColumn}.
 */
public class TableController implements PropertyChangeListener {

    private static final String LENGTH = "length";
    private static final String TITLE = "title";
    private static final String ARTIST = "artist";
    private static final String VOTE_COUNT = "voteCount";
    private static final String PLAYABLE_IN_COUNT = "playsBeforeReplay";
    private static final String VOTE_CLICK = "voteClick";
    private static final int COLUMN_COUNT = 6;

    @FXML
    private final TableView<QueueSong> table = new TableView<>();

    @FXML
    private final TableColumn<QueueSong, String> tileColumn = new TableColumn<>(TITLE);

    @FXML
    private final TableColumn<QueueSong, String> artistColumn = new TableColumn<>(ARTIST);

    @FXML
    private final TableColumn<QueueSong, String> lengthColumn = new TableColumn<>(LENGTH);

    @FXML
    private final TableColumn<QueueSong, Button> voteColumn = new TableColumn<>(VOTE_COUNT);

    @FXML
    private final TableColumn<QueueSong, String> playableInColumn = new TableColumn<>(PLAYABLE_IN_COUNT);

    @FXML
    private final TableColumn<QueueSong, Button> buttonColumn = new TableColumn<>(VOTE_CLICK);


    @FXML
    private final AnchorPane controlPane = new AnchorPane();

    @FXML
    private final AnchorPane currentSongPane = new AnchorPane();

    @FXML
    private final AnchorPane volumePane = new AnchorPane();

    private final ObservableList<QueueSong> observedSongs = FXCollections.observableArrayList(List.of());

    private VoteStrategy voteStrategy;
    private User user;

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case MusicPlayer.NEW_SONG:
                table.refresh();
                break;
            case Playlist.PLAYLIST_CHANGE:
                observedSongs.clear();
                final VoteList newVoteList = (VoteList) event.getNewValue();
                observedSongs.addAll(newVoteList.getVotedPlaylist());
                table.refresh();
                break;
            case MusicPlayer.END_PLAYER:
                observedSongs.clear();
                break;
            case Client.USER_INIT:
                user = (User) event.getNewValue();
                ActionButtonTableCell.setUser(user);
                break;
            default:
                break;
        }
    }

    /**
     * Init for the controller and view. Set up {@link #table} and connect {@link #voteStrategy} for propertyChange
     * relation.
     *
     * @param voteStrategy voteStrategy
     * @param user user who manipulate playlist
     */
    public void init(final VoteStrategy voteStrategy, final User user) {
        this.voteStrategy = voteStrategy;
        this.user = user;
        tableViewInit();
    }

    /**
     * Initialize {@link #table} as a observer of playlist.
     */
    private void tableViewInit() {
        table.setItems(observedSongs);
        setCellValueFactories();
        setTableColumnProperties();
        voteColumn.setSortable(false);
        voteColumn.setSortType(TableColumn.SortType.ASCENDING);
        buttonColumn.setSortable(false);
    }


    /**
     * Set values for each column in {@link #table}.
     */
    private void setCellValueFactories() {
        tileColumn.setCellValueFactory(new PropertyValueFactory<>(TITLE));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>(ARTIST));
        voteColumn.setCellValueFactory(new PropertyValueFactory<>(VOTE_COUNT));
        playableInColumn.setCellValueFactory(new PropertyValueFactory<>(PLAYABLE_IN_COUNT));
        buttonColumn.setCellFactory(ActionButtonTableCell.forTableColumn("Click to vote", (QueueSong song) -> {
            voteForSong(song);
            return song;
        }));

        lengthColumn.setCellValueFactory(songCallback ->
                new SimpleStringProperty(ControllerUtils.generateTimeFormat(songCallback.getValue().getLength())));
    }

    /**
     * Set size of each column in {@link #table} to have same relation if user resize window.
     */
    private void setTableColumnProperties() {
        tileColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        artistColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        lengthColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        voteColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        buttonColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        playableInColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
    }

    private void voteForSong(final QueueSong song) {
        try {
            voteStrategy.voteById(song.getId(), user);
        } catch (SongIdNotAvailable | UserVotedAlreadyException e) {
            System.out.print(e.getMessage()); //NOPMD
        }
    }

    public void setControlPane(final Pane pane) {
        controlPane.getChildren().add(pane);
    }

    public void setCurrentSongPane(final Pane pane) {
        currentSongPane.getChildren().add(pane);
    }

    public void setVolumePane(final Pane pane) {
        volumePane.getChildren().add(pane);
    }

}
