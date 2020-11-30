package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import com.nesterov.university.dao.LessonDao;
import com.nesterov.university.model.Audience;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Lesson;
import com.nesterov.university.model.LessonTime;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

	@Mock
	private LessonDao lessonDao;

	@InjectMocks
	private LessonService lessonService;

	@Spy
	List<Lesson> lessons = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		lessons.add(new Lesson(1, new Subject(1, "Literature"), new Audience(1, 14, 30), LocalDate.of(2020, 12, 31),
				new LessonTime(1, 2, LocalTime.of(8, 30), LocalTime.of(9, 45)), new Teacher("Ivan", "Ivanov",
						LocalDate.of(1999, 7, 7), "Kiev", "Ivan@Ivanov", "123456789", Gender.MALE)));
	}

	@Test
	void givenExpectedListOfExistsLessons_whenGetAll_thenRelevantListOfLessonsReturned() {
		List<Lesson> expected = new ArrayList<>();
		expected.add(lessons.get(0));
		given(lessonDao.getAll()).willReturn(expected);

		List<Lesson> actual = lessonService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedLesson_whenGet_thenRelevantLessonReturned() {
		final Lesson expected = lessons.get(0);
		given(lessonDao.get(anyLong())).willReturn(expected);

		final Lesson actual = lessonService.get(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		doNothing().when(lessonDao).delete(anyLong());

		lessonService.delete(anyLong());

		verify(lessonDao, times(1)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoGetMethodCall_whenUpdate_thenEqualCountOfDaoGetMethodCallReturned() {
		int expected = 2;
		Lesson lesson = lessons.get(0);
		doNothing().when(lessonDao).update(any(Lesson.class));

		lessonService.update(lesson);
		lessonService.update(lesson);

		verify(lessonDao, times(expected)).update(lesson);
	}

	@Test
	void givenExpectedCountOfDaoGetMethodCall_whenCreate_EqualOfDaoGetMethodCallReturned() {
		Lesson lesson = lessons.get(0);
		doThrow(new EmptyResultDataAccessException(1)).when(lessonDao).get(lesson.getId());

		lessonService.create(lessons.get(0));

		verify(lessonDao, times(1)).create(lesson);
	}
}
