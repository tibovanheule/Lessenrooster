package timetable.db;


public interface DataAccessContext extends AutoCloseable {
    ItemsDAO getItemDoa();

    LectureDAO getLectureDoa();

    PeriodDAO getPeriodDAO();

    StudentsDAO getStudentsDAO();

    LocationDAO getLocationDAO();

    TeacherDAO getTeacherDAO();


    @Override
    void close() throws DataAccessException;
}
