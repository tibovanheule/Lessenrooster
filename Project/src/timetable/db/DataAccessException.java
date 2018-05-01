package timetable.db;

import timetable.StdError;

/**
 * Custom Error message
 *
 * @author Tibo Vanheule
 */
public class DataAccessException extends Exception {

    public DataAccessException(String message, Throwable th) {
        /* DEBUGGING over, no need for full message
        super(message, th);
        This is just more user friendly ;) */
        new StdError(message + "\n");
    }

}
