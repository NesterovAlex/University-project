package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.LessonTimeDao;
import com.nesterov.university.model.LessonTime;

@ExtendWith(MockitoExtension.class)
class LessonTimeServiceTest {

	@Mock
	private LessonTimeDao lessonTimeDao;

	@InjectMocks
	private LessonTimeService lessonTimeService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void givenExpectedListOfExistsLessonTimes_whenGetAll_thenRelevantListOfLessonTimesReturned() {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(9, 15), LocalTime.of(10, 45));
		List<LessonTime> expected = new ArrayList<>();
		expected.add(lessonTime);
		given(lessonTimeDao.findAll()).willReturn(expected);

		List<LessonTime> actual = lessonTimeService.getAll();

		assertEquals(expected, actual);
		verify(lessonTimeDao).findAll();
	}

	@Test
	void givenExpectedLessonTime_whenGet_thenRelevantLessonTimeReturned() {
		LessonTime expected = new LessonTime(1, 14, LocalTime.of(7, 15), LocalTime.of(8, 45));
		given(lessonTimeDao.get(expected.getId())).willReturn(expected);

		final LessonTime actual = lessonTimeService.get(expected.getId());

		assertEquals(expected, actual);
		verify(lessonTimeDao).get(expected.getId());
	}

	@Test
	void givenExpectedIdOfLessonTime_whenDelete_thenDeleted() {
		int expected = 1;

		lessonTimeService.delete(expected);

		verify(lessonTimeDao).delete(expected);
	}

	@Test
	void givenLessonTime_whenUpdate_thenUpdated() {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(14, 15), LocalTime.of(15, 45));

		lessonTimeService.update(lessonTime);

		verify(lessonTimeDao).update(lessonTime);
	}

	@Test
	void givenExpectedCountOfDaoGetMethodCall_whenCreate_thenCreated() {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(17, 45), LocalTime.of(18, 45));

		lessonTimeService.create(lessonTime);

		verify(lessonTimeDao).create(lessonTime);
	}

	@Test
	void givenExpectedLessonTimeWithWrongStartAndEndTime_whenCreate_thenNotCreated() {
		LessonTime lessonTime = new LessonTime(1, 13, LocalTime.of(9, 15), LocalTime.of(8, 45));

		lessonTimeService.create(lessonTime);

		verify(lessonTimeDao, never()).create(lessonTime);
	}

	@Test
	void givenExpectedLessonTimeWithWrongStartAndEndTime_whenUpdate_thenNotUpdated() {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(10, 35), LocalTime.of(9, 15));

		lessonTimeService.update(lessonTime);

		verify(lessonTimeDao, never()).update(lessonTime);
	}
}
