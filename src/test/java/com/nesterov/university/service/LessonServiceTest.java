package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.LessonDao;
import com.nesterov.university.model.Audience;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Group;
import com.nesterov.university.model.Lesson;
import com.nesterov.university.model.LessonTime;
import com.nesterov.university.model.Student;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

	@Mock
	private LessonDao lessonDao;

	@InjectMocks
	private LessonService lessonService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void givenExpectedListOfExistsLessons_whenGetAll_thenRelevantListOfLessonsReturned() {
		int expected = 1;
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(lesson);
		given(lessonDao.findAll()).willReturn(lessons);

		List<Lesson> actual = lessonService.getAll();

		assertEquals(lessons, actual);
		verify(lessonDao, times(expected)).findAll();
	}

	@Test
	void givenExpectedLesson_whenGet_thenRelevantLessonReturned() {
		int expected = 1;
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		given(lessonDao.get(anyLong())).willReturn(lesson);

		Lesson actual = lessonService.get(anyLong());

		assertEquals(lesson, actual);
		verify(lessonDao, times(expected)).get(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		int expected = 1;

		lessonService.delete(anyLong());

		verify(lessonDao, times(expected)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoGetMethodCall_whenUpdate_thenEqualCountOfDaoUpdateMethodCallReturned() {
		int expected = 2;
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndTeacher(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndAudience(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);

		lessonService.update(lesson);
		lessonService.update(lesson);

		verify(lessonDao, times(expected)).update(lesson);
	}

	@Test
	void givenLessonWithSameGroupsInExpectedTime_whenUpdate_thenDaoUpdateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(lessons);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}

	@Test
	void givenLessonWithSameGroupsInExpectedTime_whenCreate_thenDaoCreateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(lessons);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenExpectedLessonWithNotEmptyAudience_whenUpdate_thenDaoUpdateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndTeacher(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndAudience(any(LocalDate.class), anyLong(), anyLong())).thenReturn(lessons);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}

	@Test
	void givenExpectedLessonWithNotEmptyAudience_whenCreate_thenDaoCreateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndTeacher(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndAudience(any(LocalDate.class), anyLong(), anyLong())).thenReturn(lessons);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenExpectedLessonWithBusyTeacher_whenUpdate_thenDaoUpdateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndTeacher(any(LocalDate.class), anyLong(), anyLong())).thenReturn(lessons);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}

	@Test
	void givenExistingLesson_whenCreate_thenEqualOfDaoCreateMethodCallReturned() {
		int expected = 1;
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndTeacher(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndAudience(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);

		lessonService.create(lesson);

		verify(lessonDao, times(expected)).create(lesson);
	}

	@Test
	void givenExpectedLessonWithSameGroupInThisTime_whenCreate_thenDaoCreateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(2, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(lessons);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenExpectedLessonWithSameGroupInThisTime_whenUpdate_thenDaoUpdateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(2, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(lessons);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}

	@Test
	void givenExpectedLessonWithBusyTeacher_whenCreate_thenDaoCreateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(2, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndTeacher(any(LocalDate.class), anyLong(), anyLong())).thenReturn(lessons);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenNonExistingLesson_whenCreate_thenDaoCreateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(2, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(2, new Subject(2, "Literature"), new Audience(1, 12, 30), LocalDate.of(2019, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), teacher);
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		groups.add(group);
		lessons.add(lesson);
		lesson.setSubject(subject);
		lesson.setTeacher(teacher);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenExpectedLessonWithWeekendDate_whenCreate_thenthenDaoCreateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(2, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2014, 07, 19),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenExpectedLessonWithWeekendDate_whenUpdate_thenthenDaoUpdateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(2, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2014, 07, 19),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}

	@Test
	void givenExpectedLessonWithTeacherWhichDontHasRightToTeach_whenCreate_thenDaoCreateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Biology");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndTeacher(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenExpectedLessonWithTeacherWhichDontHasRightToTeach_whenUpdate_thenDaoUpdateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Biology");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndTeacher(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}

	@Test
	void givenExpectedLessonWithCountOfStudentsWhichMoreThenAudienceCapacity_whenCreate_thenDaoCreateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		Stream.iterate(0, n -> n + 1).limit(100).forEach(x -> students.add(new Student()));
		students.add(student);
		group.setStudents(students);
		groups.add(group);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);

		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndTeacher(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndAudience(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenExpectedLessonWithCountOfStudentsWhichMoreThenAudienceCapacity_whenUpdate_thenDaoUpdateMethodDontCall() {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		Stream.iterate(0, n -> n + 1).limit(100).forEach(x -> students.add(new Student()));
		students.add(student);
		group.setStudents(students);
		groups.add(group);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);

		when(lessonDao.findByDateAndGroup(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndTeacher(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);
		when(lessonDao.findByDateAndAudience(any(LocalDate.class), anyLong(), anyLong())).thenReturn(null);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}
}
