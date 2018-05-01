package timetable.objects;

/**
 * Object Period, holds all period data (id, hour , minutes)
 *
 * @author Tibo Vanheule
 */
public class Period {
    private Integer hour;
    private Integer minute;
    private Integer id;

    public Period(Integer block, Integer hour, Integer minute) {
        this.id = block;
        this.hour = hour;
        this.minute = minute;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return hour + ":" + minute;
    }
}
