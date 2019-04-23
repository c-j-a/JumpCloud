package com.jc.action;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * A class that represents an Action.
 */
// Specifies the order in which the fields appear in the JSON
@JsonPropertyOrder({"action", "time"})
public class Action {

    private String name;
    private long time;

    /**
     * Gets the name of the Action.
     *
     * @return The name.
     */
    @JsonGetter("action") // This tells the JSON parser to map name to "action"
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Action.
     * @param name The name.
     */
    @JsonSetter("action") // This tells the JSON parser to map "action" to name
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the time the action took.
     *
     * @return The time.
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets the time the action took.
     *
     * @param time The time.
     */
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [name='" + name + '\'' +
                ", time=" + time +
                ']';
    }

}
