package timetable.db.mysql;

import timetable.db.DataAccessException;
import timetable.db.PeriodDAO;
import timetable.objects.Period;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlPeriodDAO extends MysqlAbstractDOA implements PeriodDAO {

    public MysqlPeriodDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<Period> getPeriods() throws DataAccessException{
        List<Period> periods = new ArrayList<>();
        String selection = "SELECT id,hour,minute FROM period ORDER BY id";
        try (Statement statement = create(); ResultSet resultSet = statement.executeQuery(selection)) {
            while (resultSet.next()) {
                periods.add(new Period(resultSet.getInt("id"),resultSet.getInt("hour"),resultSet.getInt("minute")));
            }
        } catch (Exception e) {
            //foutmelding weergeven in de lijst.
            throw new DataAccessException("could not retrieve items", e);
        }
        return periods;
    }
}
