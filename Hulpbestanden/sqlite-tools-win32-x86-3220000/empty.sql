-- Complains, but that's OK
drop table period;
drop table students;
drop table teacher;
drop table location;
drop table lecture;

create table period   ("id", "hour", "minute");
create table students ("id", "name");
create table teacher  ("id", "name");
create table location ("id", "name");
create table lecture  ("students_id", "teacher_id", "location_id", "course", "day", "first_block", "duration");
