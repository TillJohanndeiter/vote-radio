package de.techfak.gse.tjohanndeiter.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;


/**
 * Source: https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view.
 *
 * @param <S> Type of observed.
 */
public final class ActionButtonTableCell<S> extends TableCell<S, Button> {

    private final Button actionButton;

    private ActionButtonTableCell(final String label, final Function<S, S> function) {
        this.getStyleClass().add("action-button-table-cell");

        this.actionButton = new Button(label);
        this.actionButton.setOnAction((ActionEvent e) -> {
            function.apply(getCurrentItem());
        });
        this.actionButton.setMaxWidth(Double.MAX_VALUE);
    }

    private S getCurrentItem() {
        return getTableView().getItems().get(getIndex());
    }

    public static <S> Callback<TableColumn<S, Button>,
            TableCell<S, Button>> forTableColumn(final String label, final Function<S, S> function) {
        return param -> new ActionButtonTableCell<>(label, function);
    }

    @Override
    public void updateItem(final Button item, final boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(actionButton);
        }
    }

}
