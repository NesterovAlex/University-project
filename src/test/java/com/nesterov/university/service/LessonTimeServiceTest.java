package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Optional.of;
import static java.util.Optional.empty;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.LessonTimeDao;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotFoundException;
import com.nesterov.university.dao.exceptions.NotRightTimeException;
import com.nesterov.university.model.LessonTime;

@ExtendWith(MockitoExtension.class)
class LessonTimeServiceTest {

	@Mock
	private LessonTimeDao lessonTimeDao;

	@InjectMocks
	private LessonTimeService lessonTimeService;

	@Test
	void givenListOfExistsLessonTimes_whenGetAll_thenExpectedListOfLessonTimesReturned() {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(9, 15), LocalTime.of(10, 45));
		List<LessonTime> expected = new ArrayList<>();
		expected.add(lessonTime);
		given(lessonTimeDao.findAll()).willReturn(expected);

		List<LessonTime> actual = lessonTimeService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenEmptyListLessonTimes_whenGetAll_thenNotFoundEntitiesExceptionThrown() {
		given(lessonTimeDao.findAll()).willReturn(new ArrayList<>());

		assertThrows(NotFoundEntitiesException.class, () -> lessonTimeService.getAll());
	}

	@Test
	void givenLessonTime_whenGet_thenExpectedLessonTimeReturned() {
		LessonTime expected = new LessonTime(1, 14, LocalTime.of(7, 15), LocalTime.of(8, 45));
		given(lessonTimeDao.get(expected.getId())).willReturn(of(expected));

		LessonTime actual = lessonTimeService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenLessonTimeId_whenDelete_thenDeleted() {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(7, 15), LocalTime.of(8, 45));
		when(lessonTimeDao.get(lessonTime.getId())).thenReturn(of(lessonTime));

		lessonTimeService.delete(lessonTime.getId());

		verify(lessonTimeDao).delete(lessonTime.getId());
	}

	@Test
	void givenOptionalEmptyLesson_whenDelete_thenNotFoundExceptionThrown() {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(7, 15), LocalTime.of(8, 45));
		when(lessonTimeDao.get(lessonTime.getId())).thenReturn(empty());

		assertThrows(NotFoundException.class, () -> lessonTimeService.delete(lessonTime.getId()));
	}

	@Test
	void givenLessonTime_whenUpdate_thenUpdated() {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(14, 15), LocalTime.of(15, 45));

		lessonTimeService.update(lessonTime);

		verify(lessonTimeDao).update(lessonTime);
	}

	@Test
	void givenLessonTime_whenCreate_thenCreated() {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(17, 45), LocalTime.of(18, 45));

		lessonTimeService.create(lessonTime);

		verify(lessonTimeDao).create(lessonTime);
	}

	@Test
	void givenLessonTimeWithWrongStartAndEndTime_whenCreate_thenNotCreatedAndNotRightTimeExceptionThrown() {
		LessonTime lessonTime = new LessonTime(1, 13, LocalTime.of(9, 15), LocalTime.of(8, 45));

		assertThrows(NotRightTimeException.class, () -> lessonTimeService.create(lessonTime));

		verify(lessonTimeDao, never()).create(lessonTime);
	}

	@Test
	void givenLessonTimeWithWrongStartAndEndTime_whenUpdate_thenNotUpdatedAndNotRightTimeExceptionThrown() {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(10, 35), LocalTime.of(9, 15));

		assertThrows(NotRightTimeException.class, () -> lessonTimeService.update(lessonTime));

		verify(lessonTimeDao, never()).update(lessonTime);
	}
}
