package com.jc.action.service;

import com.jc.action.ActionStatistics;
import com.jc.error.Error;

/**
 * An interface defining a service to hand Action requests.
 * The Action Service will accept different types of Actions and their
 * corresponding times while keeping average times for each unique Action type.
 */
public interface ActionService {

    /**
     * Adds an Action to be processed.
     *
     * @param action A String representation of the Action (JSON, XML, etc.)
     * @return Returns an Error if one occurred, otherwise will return null
     */
    Error addAction(String action);


    /**
     * Gets the statistics for all Actions we have seen.
     *
     * @return A String containing the Action statistics
     */
    String getStats();

    /**
     * Gets an ActionStatistics for the given action name.
     * This is primarily used for testing to ensure actual values
     * match expected values.
     *
     * @return The ActionStatistics or null if none was found matching the action name.
     */
    ActionStatistics getActionStatistics(String action);

}
