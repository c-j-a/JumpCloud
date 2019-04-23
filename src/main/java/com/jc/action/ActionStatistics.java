package com.jc.action;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A class that will maintain statistical information for a
 * specific Action.
 */
// Don't use the time or count fields when serializing to JSON
@JsonIgnoreProperties({"time", "count"})
// Specifies the order in which the fields appear in the JSON
@JsonPropertyOrder({"action", "avg"})
public class ActionStatistics {

    private String name;
    private AtomicLong totalTime = new AtomicLong();  // For thread safety
    private AtomicLong totalCount = new AtomicLong(); // For thread safety


    /**
     * Creates a new set of statistics for the given action name.
     *
     * @param actionName The name of the Action
     */
    public ActionStatistics(final String actionName) {
        this.name = actionName;
    }

    /**
     * Gets the Action name.
     *
     * @return The Action name.
     */
    @JsonGetter("action") // This tells the JSON parser to map name to "action"
    public String getName() {
        return name;
    }

    /**
     * Gets the total amount of time for this action.
     *
     * @return The total time of this action.
     */
    public long getTime() {
        return totalTime.get();
    }

    /**
     * Gets the total number of occurrences of this action.
     *
     * @return The total count of this action.
     */
    public long getCount() {
        return totalCount.get();
    }

    /**
     * Gets the Average time of this action.
     *
     * @return The average time.
     */
    public long getAvg() {
        // We would probably want to return the average as a double.
        // I used long to match the assignment example results.
        return (totalCount.get() == 0) ? 0 : (totalTime.get() / totalCount.get());
    }

    /**
     * Update the Statistics for the given Action.
     *
     * @param action The Action being added
     */
    public void updateStats(final Action action) {
        totalCount.incrementAndGet();
        totalTime.addAndGet(action.getTime());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [name='" + getName() + '\'' +
                ", totalTime=" + getTime() +
                ", totalCount=" + getCount() +
                ", average=" + getAvg() +
                ']';
    }

}
