package timetable.objects;

public class Period {
    private Integer hour;
    private Integer minute;
    private Integer id;
    private Boolean changed;

    public Period(Integer block, Integer hour, Integer minute) {
        this.id = block;
        this.hour = hour;
        this.minute = minute;
        this.changed = false;
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

    public Boolean getChanged() {
        return changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }

    @Override
    public String toString() {
        return hour + ":" + minute;
    }
}
