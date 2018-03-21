package timetable.objects;

public class Lecture {
    private String student;
    private String teacher;
    private String location;
    private String course;
    private Integer day;
    private Integer block;
    private Integer duration;

    public Lecture(String student, String teacher, String location, String course, Integer day, Integer block, Integer duration){
        this.block = block;
        this.course = course;
        this.day = day;
        this.duration = duration;
        this.location = location;
        this.student = student;
        this.teacher = teacher;
    }

    public Integer getDay(){
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
