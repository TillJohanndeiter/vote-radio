package de.techfak.gse.tjohanndeiter.view;

import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.Vote;
import de.techfak.gse.tjohanndeiter.model.server.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;


/**
 * Cell with a button for making a vote in the gui.
 * Source: https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view.
 *
 * @param <S> Type of observed.
 */
public final class ActionButtonTableCell<S> extends TableCell<S, Button> {

    private static User user;
    private final Button actionButton;


    /**
     * @param label    text on the button
     * @param function action of clicked button
     */
    private ActionButtonTableCell(final String label, final Function<S, S> function) {
        this.getStyleClass().add("action-button-table-cell");
        this.actionButton = new Button(label);
        this.actionButton.setOnAction((ActionEvent e) -> function.apply(getCurrentItem()));
        this.actionButton.setMaxWidth(Double.MAX_VALUE);
    }


    public static void setUser(final User user) {
        ActionButtonTableCell.user = user;
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(
            final String label, final Function<S, S> function) {
        return param -> new ActionButtonTableCell<>(label, function);
    }


    private S getCurrentItem() {
        return getTableView().getItems().get(getIndex());
    }

    @Override
    public void updateItem(final Button item, final boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else if (userVotedAlready() || isFirstInList()) {
            actionButton.setDisable(true);
            setGraphic(actionButton);
        } else {
            setGraphic(actionButton);
        }
    }

    private boolean isFirstInList() {
        return getIndex() == 0;
    }

    private boolean userVotedAlready() {
        final QueueSong queueSong = (QueueSong) getCurrentItem();
        for (final Vote vote : queueSong.getVotes()) {
            if (vote.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }
}
