package timetable.objects;

public class Lecture {
    private String student;
    private String teacher;
    private String location;
    private String course;

    public String getTime() {
        return time;
    }

    private String time;
    private Integer day, block, duration;
    private Boolean conflict;

    public Lecture(String student, String teacher, String location, String course, Integer day, Integer block, Integer duration, String time) {
        this.block = block;
        this.course = course;
        this.day = day;
        this.duration = duration;
        this.location = location;
        this.student = student;
        this.teacher = teacher;
        this.conflict = false;
        this.time = time;
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
