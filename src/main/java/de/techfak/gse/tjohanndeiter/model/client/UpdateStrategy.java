package de.techfak.gse.tjohanndeiter.model.client;

import java.beans.PropertyChangeListener;


/**
 * Strategy for recognize changes on the server on the client side.
 */
public interface UpdateStrategy {
    String LOST_CONNECTION = "lostConnection";
    String JSON_ERROR = "jsonError";
    void stop();
    void addPropertyChangeListener(final PropertyChangeListener observer);
}
