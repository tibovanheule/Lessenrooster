package timetable.objects;

import java.util.ArrayList;
import java.util.Comparator;

public class Lecture implements Comparator<Lecture> {
    private String student, teacher, location, course, time;
    private Integer day;
    private Integer block;
    private Integer duration;
    private Integer hour;
    private Integer minute;

    public Integer getStudentId() {
        return studentId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    private Integer studentId;
    private Integer teacherId;
    private Integer locationId;
    private ArrayList<Lecture> conflicts = new ArrayList<>();
    private Boolean conflict;

    public Lecture(String student, String teacher, String location, String course, Integer day, Integer block, Integer duration, Integer hour,
                   Integer minute, Integer sutdentId, Integer teacherId, Integer locationId) {
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
        this.studentId = sutdentId;
        this.locationId = locationId;
        this.teacherId = teacherId;
    }

    public ArrayList<Lecture> getConflicts() {
        return conflicts;
    }

    public Integer getHour() {
        return hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void addConflict(Lecture lecture) {
        conflicts.add(lecture);
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

    @Override
    public int compare(Lecture o1, Lecture o2) {
        if (o1.getTime().compareTo(o2.getTime()) == 0) {
            return 0;
        } else if (o1.getHour() < o2.getHour() && o2.getHour() < o1.getHour() + o1.getDuration()) {
            return -1;
        } else if (o2.getHour() < o1.getHour() && o1.getHour() < o2.getHour() + o2.getDuration()) {
            return -1;
        } else {
            return 1;
        }
    }
}

