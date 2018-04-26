package timetable.db;


public interface DataAccessContext extends AutoCloseable {

    LectureDAO getLectureDoa();

    PeriodDAO getPeriodDAO();

    StudentsDAO getStudentsDAO();

    LocationDAO getLocationDAO();

    TeacherDAO getTeacherDAO();


    @Override
    void close() throws DataAccessException;
}
