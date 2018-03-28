DROP TABLE IF EXISTS period;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS teacher;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS lecture;

create table period   (id INT NOT NULL AUTO_INCREMENT, hour INT, minute INT, PRIMARY KEY(id));
create table students (id INT NOT NULL AUTO_INCREMENT, name TEXT, PRIMARY KEY(id));
create table teacher  (id INT NOT NULL AUTO_INCREMENT, name TEXT, PRIMARY KEY(id));
create table location (id INT NOT NULL AUTO_INCREMENT, name TEXT, PRIMARY KEY(id));
create table lecture  (students_id INT, teacher_id INT, location_id INT, course TEXT, day INT, first_block INT, duration INT
  , FOREIGN KEY(students_id) REFERENCES students(id)
  , FOREIGN KEY (teacher_id) REFERENCES teacher(id)
  , FOREIGN KEY (location_id) REFERENCES location(id)
  , FOREIGN KEY (first_block) REFERENCES period(id)
);

insert into period values (1,  8, 30)
                        , (2, 10, 0)
                        , (3, 11, 30)
                        , (4, 13, 0)
                        , (5, 14, 30)
                        , (6, 16, 0)
                        , (7, 17, 30)
                        , (8, 19, 0)
                        ;

insert into students (id, name) values (1, 'Bachelor Informatica 1')
                          , (2, 'Bachelor Informatica 2')
                          , (3, 'Bachelor Informatica 3')
                          ;

insert into teacher (id, name) values (1, 'Kris Coolsaet')
                         , (2, 'Guy De Tré')
                         , (3, 'Peter Dawyndt')
                         , (4, 'Eric Laermans')
                         , (5, 'Bart De Bruyn')
                         , (6, 'Veerle Fack')
                         , (7, 'Peter Vandendriessche')
                         , (8, 'Nicolas Van Cleemput')
                         , (9, 'Christophe Scholliers')
                         , (10, 'Filip De Turck')
                         , (11, 'Gunnar Brinkmann')
                         , (12, 'Piet Demeester')
                         , (13, 'Christophe Ley')
                         , (14, 'Peter Lambert')
                         , (15, 'Koen De Bosschere')
                         , (16, 'Marnix Van Daele')
                         , (17, 'Yvan Saeys')
                         , (18, 'Jan Goedgebeur')
                         , (19, 'Leo Storme')
                         , (20, 'Bart Dhoedt')
                         ;

insert into location (id, name) values (1, 'Sterre, S9, A0')
                          , (2, 'Sterre, S9, A1')
                          , (3, 'Sterre, S9, A2')
                          , (4, 'Sterre, S9, A3')
                          , (5, 'Sterre, S5, Grace Hopper')
                          , (6, 'Sterre, S9, Konrad Zuse')
                          , (7, 'Sterre, S25, Emmy Noether')
                          , (8, 'Sterre, S9, Alan Turing')
                          , (9, 'Sterre, S8, Victor Van Straelen')
                          , (10, 'Plateau/Rozier, Auditorium L')
                          , (11, 'Plateau/Rozier, PC A')
                          , (12, 'Plateau/Rozier, PC B')
                          , (13, 'Plateau/Rozier, PC C')
                          , (14, 'Plateau/Rozier, PC D')
                          , (15, 'Plateau/Rozier, PC E')
                          , (16, 'Plateau/Rozier, PC F')
                          , (17, 'Sterre, S12, Auditorium 1.25')
                          , (18, 'Sterre, S12, Leszaal 0.A')
                          , (19, 'Sterre, S9, Leszaal 1.1')
                          , (20, 'Sterre, S12, Practicumzaal 0.016')
                          , (21, 'Plateau/Rozier, Auditorium F')
                          , (22, 'Plateau/Rozier, Auditorium O')
                          , (23, 'Ardoyen, Auditorium 1')
                          , (24, 'Ardoyen, Practicumzaal 2.1')
                          , (25, 'Ardoyen, Practicumzaal 2.2')
                          , (26, 'Ardoyen, Practicumzaal 2.4')
                          , (27, 'Ardoyen, Studentenfoyer')
                          , (28, 'Sterre, S8, Gerardus Merator')
                          , (29, 'Sterre, S9, Leszaal 3.1')
                          , (30, 'Ardoyen 904, Leszaal 2 Magnel')
                          , (31, 'Ardoyen 914, Leszaal 0.2')
                          , (32, 'Sterre, S12, Leszaal 0.B')
                          , (33, 'Ledeganck, Auditorium 6')
                          , (34, 'UZ, PC 1.3')
                          ;

insert into lecture (students_id, teacher_id, location_id, course, day, first_block, duration) values (1, 1, 3, 'Programmeren', 1, 5, 2)
                         , (1, 2, 3, 'Databanken', 4, 2, 1)
                         , (1, 3, 3, 'Computergebruik', 2, 3,1)
                         , (1, 4, 3, 'Redeneren, abstraheren en formuleren', 2, 2,1)
                         , (1, 5, 4, 'Discrete wiskunde', 1, 2, 2)
                         , (1, 1, 3, 'Objectgericht programmeren', 3, 3, 1)
                         , (1, 6, 5, 'Algoritmen en datastructuren 1', 2, 2, 2)
                         , (1, 3, 3, 'Scriptingtalen', 1, 3, 1)
                         , (1, 7, 3, 'Lineaire algebra en meetkunde', 1, 1, 2)
                         , (1, 8, 3, 'Calculus', 1, 5, 2)
                         , (2, 9, 5, 'Functioneel programmeren', 1, 2, 2)
                         , (2, 10, 6, 'Systeemprogrammeren', 1, 5, 2)
                         , (2, 11, 3, 'Algoritmen en datastructuren 2', 2, 4, 2)
                         , (2, 12, 3, 'Communicatienetwerken', 3, 2, 2)
                         , (2, 13, 17, 'Statistiek en probabiliteit', 2, 6, 1)
                         , (2, 14, 6, 'Webdevelopment', 3, 5, 2)
                         , (2, 20, 6, 'Software Engineering Lab 1', 5, 1, 2)
                         , (2, 14, 15, 'Multimedia', 2, 1, 2)
                         , (2, 15, 10, 'Computerarchitectuur', 1, 2, 2)
                         , (2, 16, 4, 'Wetenschappelijk rekenen', 2, 3, 1)
                         , (1, 17, 18, 'Artificiële intelligentie', 1, 1, 2)
                         , (1, 11, 6, 'Algoritmen en datastructuren 3', 2, 1, 1)
                         , (1, 15, 22, 'Besturingssystemen', 1, 5, 2)
                         , (1, 9, 33, 'Logisch programmeren', 1, 5, 2)
                         , (1, 3, 5, 'Computationele biologie', 3, 1, 2)
                         , (1, 19, 6, 'Automaten, berekenbaarheid en complexiteit', 1, 1, 2)
                         , (1, 1, 5, 'Programmeren', 2, 5, 2)
                         , (1, 4, 7, 'Redeneren, abstraheren en formuleren', 3, 1, 1)
                         , (1, 5, 7, 'Discrete wiskunde', 3, 2, 2)
                         , (1, 4, 4, 'Redeneren, abstraheren en formuleren', 3, 5, 2)
                         , (1, 4, 5, 'Databanken', 4, 3, 1)
                         , (1, 3, 5, 'Computergebruik', 4, 5, 2)
                         , (1, 3, 4, 'Computergebruik', 5, 2,1)
                         , (1, 2, 1, 'Databanken', 5, 3, 1)
                         , (1, 2, 3, 'Databanken', 5, 5, 1)
                         , (1, 3, 5, 'Scriptingtalen', 2, 5, 2)
                         , (1, 8, 3, 'Calculus', 3, 1, 2)
                         , (1, 1, 5, 'Objectgericht programmeren', 3, 5, 2)
                         , (1, 1, 3, 'Objectgericht programmeren', 4, 1, 1)
                         , (1, 6, 3, 'Algoritmen en datastructuren 1', 4, 2, 2)
                         , (1, 7, 17, 'Lineaire algebra en meetkunde', 4, 6, 2)
                         , (1, 3, 3, 'Scriptingtalen', 5, 2, 1)
                         , (1, 9, 4, 'Functioneel programmeren', 2, 1, 2)
                         , (1, 10, 4, 'Systeemprogrammeren', 4, 5, 2)
                         , (1, 11, 2, 'Algoritmen en datastructuren 2', 5, 1, 1)
                         , (1, 11, 6, 'Algoritmen en datastructuren 2', 5, 2, 1)
                         , (1, 12, 11, 'Communicatienetwerken', 3, 5, 2)
                         , (1, 13, 6, 'Statistiek en probabiliteit', 4, 1, 2)
                         , (1, 13, 4, 'Statistiek en probabiliteit', 4, 4, 1)
                         , (1, 14, 2, 'Webdevelopment', 4, 2, 2)
                         , (1, 20, 17, 'Software Engineering Lab 1', 2, 5, 2)
                         , (1, 14, 10, 'Multimedia', 1, 5, 2)
                         , (1, 15, 14, 'Computerarchitectuur', 2, 1, 2)
                         , (1, 16, 17, 'Wetenschappelijk rekenen', 3, 1, 2)
                         , (1, 16, 6, 'Wetenschappelijk rekenen', 3, 3, 1)
                         , (1, 17, 4, 'Artificiële intelligentie', 4, 7, 1)
                         , (1, 11, 19, 'Algoritmen en datastructuren 3', 2, 2, 1)
                         , (1, 11, 20, 'Algoritmen en datastructuren 3', 3, 1, 2)
                         , (1, 15, 21, 'Besturingssystemen', 5, 4, 2)
                         , (1, 9, 6, 'Logisch programmeren', 4, 2, 6)
                         , (1, 3, 34, 'Computationele biologie', 5, 1, 2)
                         , (1, 19, 7, 'Automaten, berekenbaarheid en complexiteit', 2, 5, 2)
                         ;
