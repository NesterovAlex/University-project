DROP TABLE IF EXISTS audiences;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS lessonTimes;
DROP TABLE IF EXISTS lessons;
CREATE TABLE audiences
(
    id SERIAL NOT NULL,
    room_number INTEGER NOT NULL,
    capacity INTEGER NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE subjects
(
    id SERIAL NOT NULL,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE teachers 
(
    id SERIAL  NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    birth_date DATE NOT NULL,
    address VARCHAR(20) NOT NULL,
    email VARCHAR(20) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    faculty VARCHAR(20),
    course VARCHAR(20),
    PRIMARY KEY (id)
);
CREATE TABLE students 
(
    id SERIAL  NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    birth_date DATE NOT NULL,
    address VARCHAR(20) NOT NULL,
    email VARCHAR(20) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE groups
(
    id SERIAL NOT NULL,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE lessonTimes
(
    id SERIAL NOT NULL,
    order_number Integer NOT NULL,
    start_lesson TIME,
    end_lesson TIME,
    PRIMARY KEY (id)
);
CREATE TABLE lessons 
(
    id SERIAL  NOT NULL,
    subject_id INTEGER NOT NULL,
    audience_id INTEGER NOT NULL,
    lesson_date DATE NOT NULL,
    lesson_time_id INTEGER NOT NULL,
    teacher_id INTEGER NOT NULL,
    group_id INTEGER,
    PRIMARY KEY (id)
);