package timetable.objects;

public class Period {
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

    private Integer hour;
    private Integer minute;
    private Integer block;
    private Boolean changed;

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public Boolean getChanged() {
        return changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }

    public Period(Integer block, Integer hour, Integer minute) {
        this.block = block;
        this.hour = hour;
        this.minute = minute;
        this.changed = false;
    }
}
