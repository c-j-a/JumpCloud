package com.jc.error;

/**
 * A class that represents an Error and contains a message about the error.
 */
public class Error {

    String message;
    // TODO We probably want to store more information about the error
    //      like a stack trace and nested exceptions

    /**
     * Create a new Error with the provided error message.
     *
     * @param message The Error message
     */
    public Error(final String message) {
        this.message = message;
    }

    /**
     * Create a new Error from the provided Exception.
     *
     * @param e The Exception that caused the error
     */
    public Error(Exception e) {
        this.message = e.getLocalizedMessage();
        if (this.message == null) {
            this.message = e.toString();
        }
    }

    /**
     * Gets the message for this Error.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [message='" + getMessage() + "']";
    }

}
