package de.techfak.gse.tjohanndeiter.controller;

import de.techfak.gse.tjohanndeiter.model.exception.database.SongIdNotAvailable;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
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

public class TableController implements PropertyChangeListener {

    private static final String LENGTH = "length";
    private static final String TITLE = "title";
    private static final String ARTIST = "artist";
    private static final String VOTE_COUNT = "voteCount";
    private static final String PLAYABLE_IN_COUNT = "playsBeforeReplay";
    private static final String VOTE_CLICK = "voteClick";
    private static final int COLUMN_COUNT = 6;

    //TODO: Own Controller for current song

    @FXML
    private TableView<QueueSong> table = new TableView<>();

    @FXML
    private TableColumn<QueueSong, String> tileColumn = new TableColumn<>(TITLE);

    @FXML
    private TableColumn<QueueSong, String> artistColumn = new TableColumn<>(ARTIST);

    @FXML
    private TableColumn<QueueSong, String> lengthColumn = new TableColumn<>(LENGTH);

    @FXML
    private TableColumn<QueueSong, String> voteColumn = new TableColumn<>(VOTE_COUNT);

    @FXML
    private TableColumn<QueueSong, String> playableInColumn = new TableColumn<>(PLAYABLE_IN_COUNT);

    @FXML
    private TableColumn<QueueSong, Button> voteButtonColumn = new TableColumn<>(VOTE_CLICK);


    @FXML
    private AnchorPane controlPane = new AnchorPane();

    @FXML
    private AnchorPane currentSongPane = new AnchorPane();

    private ObservableList<QueueSong> observedSongs = FXCollections.observableArrayList(List.of());

    private VoteStrategy voteStrategy;


    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case Playlist.NEW_SONG:
            case VoteList.VOTE_CHANGED:
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
            default:
                break;
        }
    }

    /**
     * Init for the controller and view. Set up {@link #table} and connect {@link #voteStrategy} for propertyChange
     * relation.
     *
     * @param voteStrategy voteStrategy
     */
    public void init(final VoteStrategy voteStrategy) {
        this.voteStrategy = voteStrategy;
        tableViewInit();
    }

    private void tableViewInit() {
        table.setItems(observedSongs);
        setCellValueFactories();
        setTableColumnProperties();
        voteColumn.setSortable(false);
        voteColumn.setSortType(TableColumn.SortType.ASCENDING);
        voteButtonColumn.setSortable(false);
    }

    private void setCellValueFactories() {
        tileColumn.setCellValueFactory(new PropertyValueFactory<>(TITLE));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>(ARTIST));
        voteColumn.setCellValueFactory(new PropertyValueFactory<>(VOTE_COUNT));
        playableInColumn.setCellValueFactory(new PropertyValueFactory<>(PLAYABLE_IN_COUNT));
        voteButtonColumn.setCellFactory(ActionButtonTableCell.forTableColumn("Click to vote", (QueueSong song) -> {
            voteForSong(song);
            return song;
        }));
        lengthColumn.setCellValueFactory(songCallback ->
                new SimpleStringProperty(Controllers.generateTimeFormat(songCallback.getValue().getLength())));
    }

    private void setTableColumnProperties() {
        tileColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        artistColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        lengthColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        voteColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        voteButtonColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        playableInColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
    }

    private void voteForSong(final QueueSong song) {
        try {
            voteStrategy.voteById(song.getId());
        } catch (SongIdNotAvailable e) {
            System.out.print(e.getMessage()); //NOPMD
        }
    }

    public void setControlPane(final Pane pane) {
        controlPane.getChildren().add(pane);
    }

    public void setCurrentSongPane(final Pane pane) {
        currentSongPane.getChildren().add(pane);
    }

}
