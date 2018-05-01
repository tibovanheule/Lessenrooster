package timetable.db.sqlite;

import timetable.db.DataAccessException;
import timetable.db.PeriodDAO;
import timetable.objects.Period;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * class to edit and add periods to a sqlite database
 *
 * @author Tibo Vanheule
 */
public class SqlitePeriodDAO extends SqliteAbstractDOA implements PeriodDAO {
    SqlitePeriodDAO(Connection connection) {
        super(connection);
    }

    /**
     * get all periods
     */
    @Override
    public List<Period> getPeriods() throws DataAccessException {
        List<Period> periods = new ArrayList<>();
        String selection = "SELECT id,hour,minute FROM period ORDER BY hour";
        try (Statement statement = create(); ResultSet resultSet = statement.executeQuery(selection)) {
            while (resultSet.next()) {
                periods.add(new Period(resultSet.getInt("id"), resultSet.getInt("hour"), resultSet.getInt("minute")));
            }
        } catch (Exception e) {
            throw new DataAccessException("could not retrieve periods", e);
        }
        return periods;
    }

    /**
     * update a Period
     */
    @Override
    public int updatePeriods(Period period) throws DataAccessException {
        String update = "UPDATE period SET hour=?, minute = ? where id = ?";
        try (PreparedStatement preparedStatement = prepare(update)) {
            preparedStatement.setInt(1, period.getHour());
            preparedStatement.setInt(2, period.getMinute());
            preparedStatement.setInt(3, period.getId());
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("could not update periods", e);
        }
    }

    /**
     * delete a period
     */
    @Override
    public int deletePeriods(Period period) throws DataAccessException {
        String update = "delete from period where id = ?";
        try (PreparedStatement preparedStatement = prepare(update)) {
            preparedStatement.setInt(1, period.getId());
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("could not update periods", e);
        }
    }

    /**
     * Create a Period
     */
    @Override
    public Period createPeriod() throws DataAccessException {
        String selection = "INSERT INTO period (id,hour,minute) VALUES (?,?,?)";
        Period period = null;
        try (PreparedStatement statement = prepare(selection)) {
            statement.setInt(2, 0);
            statement.setInt(3, 0);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                period = new Period(resultSet.getInt(1), 0, 0);
            }
        } catch (Exception e) {
            throw new DataAccessException("could not retrieve periods", e);
        }
        return period;
    }


}
