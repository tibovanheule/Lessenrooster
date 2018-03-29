package timetable.db;

public interface DataAccessContext extends AutoCloseable {
    ItemsDAO getItemDoa();

    @Override
    void close() throws DataAccessException;
}
