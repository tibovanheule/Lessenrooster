package timetable.db;

import timetable.objects.Period;

import java.util.List;

public interface PeriodDAO {
    List<Period> getPeriods() throws DataAccessException;
    int updatePeriods(Period period) throws DataAccessException;
}
