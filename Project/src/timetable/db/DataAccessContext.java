package timetable.db;


public interface DataAccessContext extends AutoCloseable {
    ItemsDAO getItemDoa();

    LectureDAO getLectureDoa();

    PeriodDAO getPeriodDAO();

    @Override
    void close() throws DataAccessException;
}
