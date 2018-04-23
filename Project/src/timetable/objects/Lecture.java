package timetable.objects;

public class Lecture {
    private String student, teacher, location, course, time;
    private Integer day;
    private Integer block;
    private Integer duration;

    public Integer getHour() {
        return hour;
    }

    public Integer getMinute() {
        return minute;
    }

    private Integer hour;
    private Integer minute;
    private Boolean conflict;

    public Lecture(String student, String teacher, String location, String course, Integer day, Integer block, Integer duration, Integer hour, Integer minute) {
        this.block = block;
        this.course = course;
        this.day = day;
        this.duration = duration;
        this.location = location;
        this.student = student;
        this.teacher = teacher;
        this.conflict = false;
        this.time = hour + ":" + minute;
        this.hour = hour;
        this.minute = minute;
    }

    public String getTime() {
        return time;
    }

    public Boolean getConflict() {
        return conflict;
    }

    public void setConflict(Boolean conflict) {
        this.conflict = conflict;
    }

    public Integer getDay() {
        return day;
    }

    public Integer getBlock() {
        return block;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getCourse() {
        return course;
    }

    public String getLocation() {
        return location;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getStudent() {
        return student;
    }
}
