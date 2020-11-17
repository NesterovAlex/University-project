INSERT INTO audiences (id, room_number, capacity) VALUES (DEFAULT, 14, 87);
INSERT INTO audiences (id, room_number, capacity) VALUES (DEFAULT, 343, 187);
INSERT INTO audiences (id, room_number, capacity) VALUES (DEFAULT, 44, 67);
INSERT INTO audiences (id, room_number, capacity) VALUES (DEFAULT, 55, 897);

INSERT INTO groups (id, name) VALUES (DEFAULT, 'G-45');
INSERT INTO groups (id, name) VALUES (DEFAULT, 'Y-12');
INSERT INTO groups (id, name) VALUES (DEFAULT, 'T-56');
INSERT INTO groups (id, name) VALUES (DEFAULT, 'E-34');

INSERT INTO subjects (id, name) VALUES (DEFAULT, 'Mathematic');
INSERT INTO subjects (id, name) VALUES (DEFAULT, 'Geography');
INSERT INTO subjects (id, name) VALUES (DEFAULT, 'Geometry');
INSERT INTO subjects (id, name) VALUES (DEFAULT, 'Physic');
INSERT INTO subjects (id, name) VALUES (DEFAULT, 'Mathematic');
INSERT INTO subjects (id, name) VALUES (DEFAULT, 'Geography');
INSERT INTO subjects (id, name) VALUES (DEFAULT, 'Geometry');
INSERT INTO subjects (id, name) VALUES (DEFAULT, 'Physic');

INSERT INTO lesson_times (id, order_number, start_lesson, end_lesson) VALUES (DEFAULT, 12, '13:30', '14:20');
INSERT INTO lesson_times (id, order_number, start_lesson, end_lesson) VALUES (DEFAULT, 14, '14:45', '15:45');
INSERT INTO lesson_times (id, order_number, start_lesson, end_lesson) VALUES (DEFAULT, 16, '16:40', '17:50');
INSERT INTO lesson_times (id, order_number, start_lesson, end_lesson) VALUES (DEFAULT, 18, '18:30', '19:45');

INSERT INTO students (id, group_id, first_name, last_name, birth_date, address, email, phone, gender, faculty, course) VALUES (DEFAULT, 1, 'Bob', 'Sincler', '2012-09-17', 'Toronto', 'bob@sincler', '987654321', 'MALE', 'Biology', 'Biology');
INSERT INTO students (id, group_id, first_name, last_name, birth_date, address, email, phone, gender, faculty, course) VALUES (DEFAULT, 2, 'Vasya', 'Vasin', '2014-07-19', 'Vasino', 'Vasya@vasyin', '2354657657', 'MALE', 'Biology', 'Biology');
INSERT INTO students (id, group_id, first_name, last_name, birth_date, address, email, phone, gender, faculty, course) VALUES (DEFAULT, 3, 'Petr', 'Petrov', '2011-05-14', 'Petrovka', 'petr@petrov', '55r2346254', 'MALE', 'Biology', 'Biology');
INSERT INTO students (id, group_id, first_name, last_name, birth_date, address, email, phone, gender, faculty, course) VALUES (DEFAULT, 4, 'Ivanka', 'Ivanova', '2019-02-15', 'Ivanovo', 'ivanka@ivanova', '358769341', 'FEMALE', 'Biology', 'Biology');

INSERT INTO teachers (id, first_name, last_name, birth_date, address, email, phone, gender) VALUES (DEFAULT, 'Bob', 'Sincler', '2012-09-17', 'Toronto', 'bob@sincler', '987654321', 'MALE');
INSERT INTO teachers (id, first_name, last_name, birth_date, address, email, phone, gender) VALUES (DEFAULT, 'Vasya', 'Vasin', '2014-07-19', 'Vasino', 'Vasya@vasyin', '2354657657', 'MALE');
INSERT INTO teachers (id, first_name, last_name, birth_date, address, email, phone, gender) VALUES (DEFAULT, 'Petr', 'Petrov', '2011-05-14', 'Petrovka', 'petr@petrov', '55r2346254', 'MALE');
INSERT INTO teachers (id, first_name, last_name, birth_date, address, email, phone, gender) VALUES (DEFAULT, 'Ivanka', 'Ivanova', '2019-02-15', 'Ivanovo', 'ivanka@ivanova', '358769341', 'FEMALE');
INSERT INTO teachers (id, first_name, last_name, birth_date, address, email, phone, gender) VALUES (DEFAULT, 'Bob', 'Sincler', '2012-09-17', 'Toronto', 'bob@sincler', '987654321', 'MALE');
INSERT INTO teachers (id, first_name, last_name, birth_date, address, email, phone, gender) VALUES (DEFAULT, 'Vasya', 'Vasin', '2014-07-19', 'Vasino', 'Vasya@vasyin', '2354657657', 'MALE');
INSERT INTO teachers (id, first_name, last_name, birth_date, address, email, phone, gender) VALUES (DEFAULT, 'Petr', 'Petrov', '2011-05-14', 'Petrovka', 'petr@petrov', '55r2346254', 'MALE');
INSERT INTO teachers (id, first_name, last_name, birth_date, address, email, phone, gender) VALUES (DEFAULT, 'Ivanka', 'Ivanova', '2019-02-15', 'Ivanovo', 'ivanka@ivanova', '358769341', 'FEMALE');

INSERT INTO lessons (id, subject_id, audience_id, lesson_date, lesson_time_id, teacher_id) VALUES (DEFAULT, 1, 3, '2012-09-17', 4, 2);
INSERT INTO lessons (id, subject_id, audience_id, lesson_date, lesson_time_id, teacher_id) VALUES (DEFAULT, 4, 1, '2014-07-19', 1, 3);
INSERT INTO lessons (id, subject_id, audience_id, lesson_date, lesson_time_id, teacher_id) VALUES (DEFAULT, 1, 4, '2011-05-14', 1, 4);
INSERT INTO lessons (id, subject_id, audience_id, lesson_date, lesson_time_id, teacher_id) VALUES (DEFAULT, 1, 3, '2019-02-15', 1, 2);


INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (3, 1);
INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (3, 2);
INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (2, 1);
INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (2, 2);
INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (1, 2);

INSERT INTO lessons_groups (lesson_id, group_id) VALUES (3, 1);
INSERT INTO lessons_groups (lesson_id, group_id) VALUES (3, 2);
INSERT INTO lessons_groups (lesson_id, group_id) VALUES (2, 1);
INSERT INTO lessons_groups (lesson_id, group_id) VALUES (1, 2);