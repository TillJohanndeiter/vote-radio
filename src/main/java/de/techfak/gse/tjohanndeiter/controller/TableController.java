package de.techfak.gse.tjohanndeiter.controller;

import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.exception.database.SongIdNotAvailable;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.voting.VoteStrategy;
import de.techfak.gse.tjohanndeiter.view.ActionButtonTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.util.List;

public class TableController implements PropertyChangeListener {

    private static final String LENGTH = "length";
    private static final String TITLE = "title";
    private static final String ARTIST = "artist";
    private static final String VOTE_COUNT = "voteCount";
    private static final String VOTE_CLICK = "voteClick";
    private static final String DEFAULT_COVER = "defaultCover.png";
    private static final int COLUMN_COUNT = 5;

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
    private TableColumn<QueueSong, Button> voteButtonColumn = new TableColumn<>(VOTE_CLICK);

    @FXML
    private TextField titleOfCurrentSong = new TextField();

    @FXML
    private TextField artistOfCurrentSong = new TextField();

    @FXML
    private TextField lengthOfCurrentSong = new TextField();

    @FXML
    private TextField genreOfCurrentSong = new TextField();

    @FXML
    private ImageView coverCurrentSong;

    @FXML
    private AnchorPane networkPane = new AnchorPane();

    private ObservableList<QueueSong> observedSongs = FXCollections.observableArrayList(List.of());

    private VoteStrategy voteStrategy;

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
        tileColumn.setCellValueFactory(new PropertyValueFactory<>(TITLE));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>(ARTIST));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>(LENGTH));
        voteColumn.setCellValueFactory(new PropertyValueFactory<>(VOTE_COUNT));
        voteButtonColumn.setCellFactory(ActionButtonTableCell.forTableColumn("Click to vote", (QueueSong song) -> {
            voteForSong(song);
            return song;
        }));
        tileColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        artistColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        lengthColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        voteColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        voteButtonColumn.prefWidthProperty().bind(table.widthProperty().divide(COLUMN_COUNT));
        voteColumn.setSortable(false);
        voteColumn.setSortType(TableColumn.SortType.ASCENDING);
        voteButtonColumn.setSortable(false);
    }


    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case Playlist.NEW_SONG:
                handleNewSong(event);
                break;
            case Playlist.PLAYLIST_CHANGE:
                observedSongs.clear();
                final VoteList newVoteList = (VoteList) event.getNewValue();
                observedSongs.addAll(newVoteList.getVotedPlaylist());
                table.refresh();
                break;
            case VoteList.VOTE_CHANGED:
                table.refresh();
                break;
            case MusicPlayer.END_PLAYER:
                resetCurrentSong();
                observedSongs.clear();
                break;
            default:
                break;
        }
    }

    private void resetCurrentSong() {
        titleOfCurrentSong.setText(null);
        artistOfCurrentSong.setText(null);
        lengthOfCurrentSong.setText(null);
        genreOfCurrentSong.setText(null);
    }

    private void handleNewSong(final PropertyChangeEvent propertyChangeEvent) {
        final Song newSong = (Song) propertyChangeEvent.getNewValue();
        updateMetaDataOfCurrentSong(newSong);
        table.refresh();
    }


    private void updateMetaDataOfCurrentSong(final Song newSong) {
        titleOfCurrentSong.setText(newSong.getTitle());
        artistOfCurrentSong.setText(newSong.getArtist());
        lengthOfCurrentSong.setText(String.valueOf(newSong.getLength()));
        genreOfCurrentSong.setText(newSong.getGenre());
        if (newSong.getCover() != null) {
            coverCurrentSong.setImage(new Image(new ByteArrayInputStream(newSong.getCover())));
        } else {
            //coverCurrentSong.setImage(new Image(Objects.requireNonNull(Thread.currentThread().
            //getContextClassLoader().getResource(DEFAULT_COVER)).getPath()));

            String test = Thread.currentThread().getContextClassLoader().getResource(DEFAULT_COVER).getPath();
            System.out.println(test);
        }
    }

    private void voteForSong(final QueueSong song) {
        try {
            voteStrategy.voteById(song.getId());
        } catch (SongIdNotAvailable e) {
            System.out.print(e.getMessage()); //NOPMD
        }
    }

    public void changePanelPane(final Pane pane) {
        networkPane.getChildren().add(pane);
    }

}
