package com.jc.action;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class that manages Statistics for Actions. Statistics will be kept
 * for each unique Action based on its name.
 */
public class ActionStatisticsManager {

    // ConcurrentHashMap for thread safety
    // Map is keyed by Action name ('jump', 'run', etc.)
    private final Map<String, ActionStatistics> actionsMap = new ConcurrentHashMap<>();

    /**
     * Add an Action to update statistics on.
     *
     * @param action The Action to add
     */
    public void addAction(final Action action) {
        if (action != null) {
            // If we don't already have an ActionStatistics for this action,
            // create one and add it to the Map, otherwise just get from map.
            // Then update the ActionStatistics with the new Action
            actionsMap.computeIfAbsent(action.getName(), stats -> new ActionStatistics(action.getName()))
                      .updateStats(action);
        }
    }

    /**
     * Gets an ActionStatistics for the given action name.
     *
     * @return The ActionStatistics or null if none was found matching the action name.
     */
    public ActionStatistics getActionStatistics(final String key) {
        if (key != null) {
            return actionsMap.get(key);
        }
        return null;
    }

    /**
     * Gets a read only Collection of ActionStatistics
     *
     * @return The Collection of ActionStatistics
     */
    public Collection<ActionStatistics> getActionStatistics() {
        return Collections.unmodifiableCollection(actionsMap.values());
    }

}
