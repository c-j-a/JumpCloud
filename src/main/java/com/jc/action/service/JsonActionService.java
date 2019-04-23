package com.jc.action.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.action.Action;
import com.jc.action.ActionStatistics;
import com.jc.action.ActionStatisticsManager;
import com.jc.error.Error;

import java.io.IOException;

/**
 * A JSON Implementation of the ActionService
 */
public class JsonActionService implements ActionService {

    // Used to serialize and deserialize JSON and Java objects
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final ActionStatisticsManager statsManager = new ActionStatisticsManager();


    @Override
    public Error addAction(final String actionJson) {
        try {
            Action action = createAction(actionJson);
            statsManager.addAction(action);
            // Future consideration...
            // If the addAction were to get more complex and take more time
            // to run, I would recommend we add the Action to a BlockingQueue
            // and return. Then have a local worker thread process the queue.
            // If this were a web service call we wouldn't want the request
            // thread doing all the work while the client waits.
        }
        catch (Exception e) {
            // Invalid JSON maybe?
            return new Error(e);
        }
        return null;
    }

    @Override
    public String getStats() {
        try {
            return statsToJson();
        }
        catch (Exception e) {
            return "Error getting statistics";
        }
    }

    @Override
    public ActionStatistics getActionStatistics(final String action) {
        return statsManager.getActionsMap().get(action);
    }

    /**
     * Helper method that will convert a JSON string to a java Action
     *
     * @param jsonString The JSON String
     * @return The Action object
     * @throws IOException An exception if the String is not parsable
     */
    private static Action createAction(final String jsonString) throws IOException {
        return objectMapper.readValue(jsonString, Action.class);
    }

    /**
     * Helper to convert the collection of ActionStatistics to a JSON String.
     *
     * @return the JSON String
     * @throws JsonProcessingException If unable to serialize to JSON
     */
    private String statsToJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(statsManager.getActionsMap().values());
        // Pretty version...
        // return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(statsManager.getActionsMap().values());
    }

}
