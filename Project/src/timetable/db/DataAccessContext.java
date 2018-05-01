package timetable.db;


/**
 * interface to specify methods of DataAccessContext
 *
 * @author Tibo Vanheule
 */
public interface DataAccessContext extends AutoCloseable {

    LectureDAO getLectureDoa();

    PeriodDAO getPeriodDAO();

    StudentsDAO getStudentsDAO();

    LocationDAO getLocationDAO();

    TeacherDAO getTeacherDAO();


    @Override
    void close() throws DataAccessException;
}
