package timetable.db;

public class DataAccessException extends Exception {

    public DataAccessException(String message, Throwable th) {
        super (message, th);
    }

}
