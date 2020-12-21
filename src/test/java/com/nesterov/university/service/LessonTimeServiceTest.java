package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.LessonTimeDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.LessonTime;

@ExtendWith(MockitoExtension.class)
class LessonTimeServiceTest {

	@Mock
	private LessonTimeDao lessonTimeDao;

	@InjectMocks
	private LessonTimeService lessonTimeService;

	@Test
	void givenListOfExistsLessonTimes_whenGetAll_thenExpectedListOfLessonTimesReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(9, 15), LocalTime.of(10, 45));
		List<LessonTime> expected = new ArrayList<>();
		expected.add(lessonTime);
		given(lessonTimeDao.findAll()).willReturn(expected);

		List<LessonTime> actual = lessonTimeService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenLessonTime_whenGet_thenExpectedLessonTimeReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		LessonTime expected = new LessonTime(1, 14, LocalTime.of(7, 15), LocalTime.of(8, 45));
		given(lessonTimeDao.get(expected.getId())).willReturn(expected);

		final LessonTime actual = lessonTimeService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenLessonTimeId_whenDelete_thenDeleted() throws NotExistException {
		int lessonTimeId = 1;

		lessonTimeService.delete(lessonTimeId);

		verify(lessonTimeDao).delete(lessonTimeId);
	}

	@Test
	void givenLessonTime_whenUpdate_thenUpdated() throws NotCreateException {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(14, 15), LocalTime.of(15, 45));

		lessonTimeService.update(lessonTime);

		verify(lessonTimeDao).update(lessonTime);
	}

	@Test
	void givenLessonTime_whenCreate_thenCreated() throws NotCreateException {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(17, 45), LocalTime.of(18, 45));

		lessonTimeService.create(lessonTime);

		verify(lessonTimeDao).create(lessonTime);
	}

	@Test
	void givenLessonTimeWithWrongStartAndEndTime_whenCreate_thenNotCreated() throws NotCreateException {
		LessonTime lessonTime = new LessonTime(1, 13, LocalTime.of(9, 15), LocalTime.of(8, 45));

		lessonTimeService.create(lessonTime);

		verify(lessonTimeDao, never()).create(lessonTime);
	}

	@Test
	void givenLessonTimeWithWrongStartAndEndTime_whenUpdate_thenNotUpdated() throws NotCreateException {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(10, 35), LocalTime.of(9, 15));

		lessonTimeService.update(lessonTime);

		verify(lessonTimeDao, never()).update(lessonTime);
	}
}
