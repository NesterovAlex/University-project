DROP TABLE IF EXISTS lessons_groups CASCADE;
DROP TABLE IF EXISTS audiences CASCADE;
DROP TABLE IF EXISTS subjects CASCADE;
DROP TABLE IF EXISTS teachers CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS lesson_times CASCADE;
DROP TABLE IF EXISTS lessons CASCADE; 
DROP TABLE IF EXISTS teachers_subjects CASCADE;
CREATE TABLE audiences
(
    id SERIAL NOT NULL,
    room_number INTEGER NOT NULL,
    capacity INTEGER NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE groups
(
    id SERIAL NOT NULL,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE subjects
(
    id SERIAL NOT NULL,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE lesson_times
(
    id SERIAL NOT NULL,
    order_number Integer NOT NULL,
    start_lesson TIME,
    end_lesson TIME,
    PRIMARY KEY (id)
);
CREATE TABLE teachers 
(
    id SERIAL NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    birth_date DATE NOT NULL,
    address VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    faculty VARCHAR(20),
    course VARCHAR(20),
    PRIMARY KEY (id)
);
CREATE TABLE students 
(
    id SERIAL  NOT NULL,
    group_id INT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    address VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(100) NOT NULL,
    gender VARCHAR(100) NOT NULL,
    faculty VARCHAR(100) NOT NULL,
    course VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE lessons 
(
    id SERIAL  NOT NULL,
    subject_id INT NOT NULL,
    audience_id INT NOT NULL,
    lesson_date DATE NOT NULL,
    lesson_time_id INT NOT NULL,
    teacher_id INT NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE teachers_subjects
(
    teacher_id INT NOT NULL,
    subject_id INT NOT NULL,
    FOREIGN KEY(teacher_id) REFERENCES teachers(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(subject_id) REFERENCES subjects(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT teachers_subjects_constraint UNIQUE (teacher_id, subject_id)
);
CREATE TABLE lessons_groups
(
    lesson_id INT NOT NULL,
    group_id INT NOT NULL,
    FOREIGN KEY(lesson_id) REFERENCES lessons(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(group_id) REFERENCES groups(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT lessons_groups_constraint UNIQUE (lesson_id, group_id)
);