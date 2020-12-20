package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.LessonDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
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

	@Test
	void givenListOfExistsLessons_whenGetAll_thenExpectedListOfLessonsReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		Lesson lesson = new Lesson(1, new Subject(8, "Statistics"), new Audience(1, 14, 30), LocalDate.of(2019, 11, 30),
				new LessonTime(3, 3, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Nicholas", "Owen",
						LocalDate.of(1995, 10, 19), "Owens", "Nicholas@Owen", "495873485", Gender.MALE));
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(lesson);
		given(lessonDao.findAll()).willReturn(lessons);

		List<Lesson> actual = lessonService.getAll();

		assertEquals(lessons, actual);
	}

	@Test
	void givenLesson_whenGet_thenExpectedLessonReturned() throws EntityNotFoundException, QueryNotExecuteException {
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2018, 10, 29),
				new LessonTime(3, 2, LocalTime.of(12, 40), LocalTime.of(13, 45)), new Teacher("Gavin", "Brayden",
						LocalDate.of(1996, 5, 5), "Tyler", "Gavin@Brayden", "849483726", Gender.MALE));
		given(lessonDao.get(lesson.getId())).willReturn(lesson);

		Lesson actual = lessonService.get(lesson.getId());

		assertEquals(lesson, actual);
	}

	@Test
	void givenLessonId_whenDelete_thenDeleted() throws NotExistException {
		int lessonId = 1;

		lessonService.delete(lessonId);

		verify(lessonDao).delete(lessonId);
	}

	@Test
	void givenLesson_whenUpdate_thenUpdated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> empty = new ArrayList<Lesson>();
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(9, "Technology");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("David", "Logan", LocalDate.of(2017, 2, 15), "Emswile", "David@Logan",
				"3948576238", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(9, "Technology"), new Audience(1, 14, 30), LocalDate.of(2020, 10, 28),
				new LessonTime(1, 2, LocalTime.of(9, 40), LocalTime.of(10, 45)), new Teacher("Joseph", "Jackson",
						LocalDate.of(2006, 11, 3), "Liam", "Joseph@Jackson", "2938465743", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(4, "Psychology");
		Student student = new Student("Christian", "Jonathan", LocalDate.of(2001, 4, 19), "Vasino",
				"Christian@Jonathan", "2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(empty);
		when(lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId()))
				.thenReturn(empty);
		when(lessonDao.findByDateAndAudience(lesson.getDate(), lesson.getTime().getId(), lesson.getAudience().getId()))
				.thenReturn(empty);

		lessonService.update(lesson);

		verify(lessonDao).update(lesson);
	}

	@Test
	void givenLessonWithSameGroupsInExpectedTime_whenUpdate_thenNotUpdated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(8, "Engineering");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Jacob", "Jacoby", LocalDate.of(2009, 1, 19), "Lestervile", "Jacob@Jacoby",
				"2354657657", Gender.MALE);
		teacher.setId(6);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(8, "Engineering"), new Audience(1, 14, 30),
				LocalDate.of(2020, 12, 31), new LessonTime(1, 2, LocalTime.of(13, 30), LocalTime.of(14, 45)),
				new Teacher("Mason", "Jayden", LocalDate.of(1996, 9, 17), "Kenwood", "Mason@Jayden", "384756329",
						Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Engineering");
		Student student = new Student("Andrew", "James", LocalDate.of(2014, 07, 19), "Vasino", "Andrew@James",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		groups.add(group);
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(lessons);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}

	@Test
	void givenLessonWithSameGroupsInExpectedTime_whenCreate_thenNotCreated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(7, "Philosophy");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(4, 33);
		audience.setId(3);
		Teacher teacher = new Teacher("Krenk", "Krow", LocalDate.of(2016, 6, 9), "Krown", "Krenk@Krow", "3746529384",
				Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(7, "Philosophy"), new Audience(2, 12, 23), LocalDate.of(2011, 11, 11),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Sophiya", "Samson",
						LocalDate.of(1998, 3, 6), "Sophiya", "Sophiya@Samson", "2938476653", Gender.FEMALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(6, "Phisic");
		Student student = new Student("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		groups.add(group);
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(lessons);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenLessonWithNotEmptyAudience_whenUpdate_thenNotUpdated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> empty = new ArrayList<Lesson>();
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(3, "Geometry");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(41, 60);
		audience.setId(6);
		Teacher teacher = new Teacher("John", "Snow", LocalDate.of(2016, 3, 10), "Snowino", "John@Snow", "394756293",
				Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(3, "Geometry"), new Audience(1, 14, 30), LocalDate.of(2020, 1, 3),
				new LessonTime(1, 2, LocalTime.of(7, 30), LocalTime.of(8, 45)), new Teacher("Dmitro", "Dmitrov",
						LocalDate.of(1998, 6, 5), "Odessa", "Dmitro@Dmitrov", "4758345768", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(4, "Mathematic");
		Student student = new Student("Maksim", "Maksimov", LocalDate.of(2011, 4, 13), "Maksimovka", "Maksim@Maksimov",
				"748593293", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(empty);
		when(lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId()))
				.thenReturn(empty);
		when(lessonDao.findByDateAndAudience(lesson.getDate(), lesson.getTime().getId(), lesson.getAudience().getId()))
				.thenReturn(lessons);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}

	@Test
	void givenLessonWithNotEmptyAudience_whenCreate_thenNotCreated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> empty = new ArrayList<Lesson>();
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Evgen", "Evgeniev", LocalDate.of(2014, 07, 19), "Evgenivka", "Evgen@Evgeniev",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2017, 2, 3),
				new LessonTime(1, 2, LocalTime.of(10, 30), LocalTime.of(11, 45)), new Teacher("Ross", "Geller",
						LocalDate.of(1999, 7, 7), "Lvov", "Ivan@Ivanov", "74653928746", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Geography");
		Student student = new Student("Artem", "Artemiv", LocalDate.of(2016, 8, 9), "Artemivka", "Artem@Artemiv",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(empty);
		when(lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId()))
				.thenReturn(empty);
		when(lessonDao.findByDateAndAudience(lesson.getDate(), lesson.getTime().getId(), lesson.getAudience().getId()))
				.thenReturn(lessons);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenLessonWithBusyTeacher_whenUpdate_thenNotUpdated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(7, "Languages");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(40, 60);
		audience.setId(3);
		Teacher teacher = new Teacher("Julian", "Aaron", LocalDate.of(2013, 06, 18), "Emen", "Julian@Aaron",
				"3948573645", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(7, "Languages"), new Audience(2, 13, 29), LocalDate.of(2019, 10, 25),
				new LessonTime(3, 5, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Jordan", "	Brandon",
						LocalDate.of(1998, 6, 6), "Brandson", "Jordan@Brandon", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Angel", "Cameron", LocalDate.of(2011, 5, 15), "Los-Angeles", "Angel@Cameron",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(new ArrayList<>());
		when(lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId()))
				.thenReturn(lessons);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}

	@Test
	void givenExistingLesson_whenCreate_thenCreated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> empty = new ArrayList<Lesson>();
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(4, " Development");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(23, 47);
		audience.setId(3);
		Teacher teacher = new Teacher("Rober", "Charles", LocalDate.of(2012, 2, 19), "Charlstown", "Rober@Charles",
				"947835467", Gender.MALE);
		teacher.setId(9);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(4, " Development"), new Audience(1, 14, 30),
				LocalDate.of(2020, 12, 31), new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)),
				new Teacher("Thomas", "Zachary", LocalDate.of(1992, 2, 17), "Thomsk", "Thomas@Zachary", "495867493",
						Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Jose", "Levi", LocalDate.of(2015, 5, 15), "Lenn", "Jose@Levi", "2354657657",
				Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(empty);
		when(lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId()))
				.thenReturn(empty);
		when(lessonDao.findByDateAndAudience(lesson.getDate(), lesson.getTime().getId(), lesson.getAudience().getId()))
				.thenReturn(empty);

		lessonService.create(lesson);

		verify(lessonDao).create(lesson);
	}

	@Test
	void givenLessonWithSameGroup_whenCreate_thenNotCreated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(2, "Environment");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(40, 60);
		audience.setId(3);
		Teacher teacher = new Teacher("Chase", "Ayden", LocalDate.of(2012, 4, 14), "Jason", "Chase@Ayden", "8458392485",
				Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Environment"), new Audience(1, 14, 30),
				LocalDate.of(2020, 12, 31), new LessonTime(1, 2, LocalTime.of(11, 35), LocalTime.of(12, 45)),
				new Teacher("Kevin", "Sebastian", LocalDate.of(1995, 3, 3), "Sendvin", "Kevin@Sebastian", "495867439",
						Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(9, "Design");
		Student student = new Student("Bentley", "Dominic", LocalDate.of(2014, 07, 19), "Benn", "Bentley@Dominic",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(lessons);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenLessonWithSameGroup_whenUpdate_thenNotUpdated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(4, "Building");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(32, 32);
		audience.setId(7);
		Teacher teacher = new Teacher("Xavier", "Oliver", LocalDate.of(2013, 3, 13), "Josiah", "Xavier@Oliver",
				"493847563", Gender.MALE);
		teacher.setId(8);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(5, "Building"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Adam", "Cooper",
						LocalDate.of(1999, 7, 7), "	Carson", "Ivan@Ivanov", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(6, "Arts");
		Student student = new Student("Tristan", "Luis", LocalDate.of(2014, 07, 19), "Jaxon", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(lessons);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}

	@Test
	void givenLessonWithBusyTeacher_whenCreate_thenNotCreated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(2, "Humanities");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(40, 45);
		audience.setId(3);
		Teacher teacher = new Teacher("Mark", "Camden", LocalDate.of(2014, 07, 19), "Kaiden", "Mark@Camden",
				"894385930294", Gender.MALE);
		teacher.setId(6);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Humanities"), new Audience(2, 13, 29), LocalDate.of(2018, 12, 14),
				new LessonTime(6, 6, LocalTime.of(13, 35), LocalTime.of(14, 45)), new Teacher("Brady", "Caden",
						LocalDate.of(1993, 1, 17), "Maxwell", "Brady@Caden", "203948576", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(1, "Phisic");
		Student student = new Student("Alejandro", "Joel", LocalDate.of(2016, 6, 16), "Ashton", "Alejandro@Joel",
				"2354657657", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(new ArrayList<>());
		when(lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId()))
				.thenReturn(lessons);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenLessonWithWeekendDate_whenCreate_thenNotCreated()
			throws NotCreateException, EntityNotFoundException, QueryNotExecuteException {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(2, "Arts");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(33, 33);
		audience.setId(9);
		Teacher teacher = new Teacher("Oscar", "Malachi", LocalDate.of(2019, 9, 9), "Peyton", "Oscar@Malachi",
				"3849584693", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Arts"), new Audience(2, 22, 22), LocalDate.of(2014, 07, 19),
				new LessonTime(6, 6, LocalTime.of(16, 20), LocalTime.of(17, 45)), new Teacher("Nicolas", "Maddox",
						LocalDate.of(1995, 5, 3), "Kenneth", "Nicolas@Maddox", "4958372945", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(5, "Humanities");
		Student student = new Student("Conner", "Andres", LocalDate.of(2011, 4, 15), "Lincoln", "Conner@Andres",
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
	void givenLessonWithWeekendDate_whenUpdate_thenNotUpdated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(2, "Languages");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(22, 55);
		audience.setId(3);
		Teacher teacher = new Teacher("Derek", "Tanner", LocalDate.of(2018, 8, 8), "Silas", "Derek@Tanner",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Languages"), new Audience(3, 21, 21), LocalDate.of(2013, 6, 19),
				new LessonTime(6, 2, LocalTime.of(13, 00), LocalTime.of(14, 00)), new Teacher("Eduardo", "Seth",
						LocalDate.of(1994, 4, 3), "Jaiden", "Eduardo@Seth", "4859683745", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(3, "Health");
		Student student = new Student("Paul", "	Jorge", LocalDate.of(2014, 07, 19), "Travis", "Paul@Jorge",
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
	void givenLessonWithTeacherWhichDontHasRightToTeach_whenCreate_thenNotCreated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> lessons = new ArrayList<>();
		Subject subject = new Subject(1, "Design");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(11, 20);
		audience.setId(3);
		Teacher teacher = new Teacher("Abraham", "	Omar", LocalDate.of(2010, 1, 10), "Javier", "Abraham@Omar",
				"495867384", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(2, "Design"), new Audience(1, 14, 30), LocalDate.of(2017, 5, 25),
				new LessonTime(1, 2, LocalTime.of(16, 35), LocalTime.of(17, 45)), new Teacher("Ezekiel", "Tucker",
						LocalDate.of(1994, 2, 6), "Harrison", "Ezekiel@Tucker", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(7, "Building");
		Student student = new Student("Peter", "Damien", LocalDate.of(2015, 5, 15), "Greyson", "Peter@Damien",
				"596847345", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(new ArrayList<>());
		when(lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId()))
				.thenReturn(new ArrayList<>());

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenLessonWithTeacherWhichDontHasRightToTeach_whenUpdate_thenNotUpdated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> lessons = new ArrayList<>();
		List<Lesson> empty = new ArrayList<Lesson>();
		Subject subject = new Subject(1, "Planning");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(23, 27);
		audience.setId(3);
		Teacher teacher = new Teacher("Avery", "Weston", LocalDate.of(2015, 7, 14), "Weston", "Avery@Weston",
				"5968493856", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(3, new Subject(1, "Literature"), new Audience(4, 43, 23), LocalDate.of(2015, 3, 14),
				new LessonTime(3, 2, LocalTime.of(15, 30), LocalTime.of(16, 45)), new Teacher("Fernando", "Calvin",
						LocalDate.of(1987, 5, 11), "Kiev", "Fernando@Calvin", "3948576382", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(4, "Humanities");
		Student student = new Student("Ezra", "Xander", LocalDate.of(2011, 4, 14), "Jaylen", "Ezra@Xander", "394857693",
				Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		group.setStudents(students);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);
		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(empty);
		when(lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId()))
				.thenReturn(empty);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}

	@Test
	void givenLessonWithCountOfStudentsWhichMoreThenAudienceCapacity_whenCreate_thenNotCreated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> lessons = new ArrayList<>();
		List<Lesson> empty = new ArrayList<Lesson>();
		Subject subject = new Subject(1, "Business");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(34, 34);
		audience.setId(7);
		Teacher teacher = new Teacher("Maximus", "Josue", LocalDate.of(2013, 3, 3), "Trenton", "Maximus@Josue",
				"9658394956", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(1, new Subject(1, "Business"), new Audience(6, 15, 23), LocalDate.of(2015, 4, 24),
				new LessonTime(7, 7, LocalTime.of(18, 30), LocalTime.of(19, 45)), new Teacher("Cesar", "Chance",
						LocalDate.of(1994, 4, 5), "Zane", "Cesar@Chance", "9568594093", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(7, "Humanities");
		Student student = new Student("Emmett", "Jayce", LocalDate.of(2014, 07, 19), "Mario", "Emmett@Jayce",
				"596874386", Gender.MALE);
		List<Student> students = new ArrayList<>();
		Stream.iterate(0, n -> n + 1).limit(100).forEach(x -> students.add(new Student()));
		students.add(student);
		group.setStudents(students);
		groups.add(group);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);

		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(empty);
		when(lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId()))
				.thenReturn(empty);
		when(lessonDao.findByDateAndAudience(lesson.getDate(), lesson.getTime().getId(), lesson.getAudience().getId()))
				.thenReturn(empty);

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	void givenLessonWithCountOfStudentsWhichMoreThenAudienceCapacity_whenUpdate_thenNotUpdated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		List<Lesson> lessons = new ArrayList<>();
		List<Lesson> empty = new ArrayList<Lesson>();
		Subject subject = new Subject(1, "Technology");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(49, 47);
		audience.setId(9);
		Teacher teacher = new Teacher("Manuel", "Theodore", LocalDate.of(2019, 7, 9), "Braylon", "Manuel@Theodore",
				"483945867", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		Lesson lesson = new Lesson(4, new Subject(1, "Technology"), new Audience(1, 14, 30), LocalDate.of(2014, 4, 3),
				new LessonTime(8, 9, LocalTime.of(13, 30), LocalTime.of(14, 45)), new Teacher("Raymond", "Edwin",
						LocalDate.of(1996, 6, 6), "Abel", "Raymond@Edwin", "123456789", Gender.MALE));
		List<Group> groups = new ArrayList<>();
		Group group = new Group(9, "Science");
		Student student = new Student("Johnathan", "Alexis", LocalDate.of(2015, 5, 5), "Zion", "Johnathan@Alexis",
				"48596745", Gender.MALE);
		List<Student> students = new ArrayList<>();
		Stream.iterate(0, n -> n + 1).limit(100).forEach(x -> students.add(new Student()));
		students.add(student);
		group.setStudents(students);
		groups.add(group);
		lesson.setGroups(groups);
		lessons.add(lesson);
		lesson.setTeacher(teacher);

		when(lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId())).thenReturn(empty);
		when(lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId()))
				.thenReturn(empty);
		when(lessonDao.findByDateAndAudience(lesson.getDate(), lesson.getTime().getId(), lesson.getAudience().getId()))
				.thenReturn(empty);

		lessonService.update(lesson);

		verify(lessonDao, never()).update(lesson);
	}
}
