package de.techfak.gse.tjohanndeiter.model.client;

import java.beans.PropertyChangeListener;

public interface RequesterStrategy {
    String LOST_CONNECTION = "lostConnection";
    String JSON_ERROR = "jsonError";
    void stop();
    void addPropertyChangeListener(final PropertyChangeListener observer);
}
