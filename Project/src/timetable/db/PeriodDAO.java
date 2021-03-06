package timetable.db;

import timetable.objects.Period;

import java.util.List;

/**
 * specifies methods of PeriodDAO
 *
 * @author Tibo Vanheule
 */
public interface PeriodDAO {
    List<Period> getPeriods() throws DataAccessException;

    int updatePeriods(Period period) throws DataAccessException;

    int deletePeriods(Period period) throws DataAccessException;

    Period createPeriod() throws DataAccessException;
}
