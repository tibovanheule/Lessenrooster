package timetable.db;


public interface DataAccessContext extends AutoCloseable {
    ItemsDAO getItemDoa();

    LectureDAO getLectureDoa();

    @Override
    void close() throws DataAccessException;
}
