package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
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
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(8, 15), LocalTime.of(9, 45));
		List<LessonTime> expected = new ArrayList<>();
		expected.add(lessonTime);
		given(lessonTimeDao.findAll()).willReturn(expected);

		List<LessonTime> actual = lessonTimeService.getAll();

		assertEquals(expected, actual);
		verify(lessonTimeDao, times(1)).findAll();
	}

	@Test
	void givenExpectedLessonTime_whenGet_thenRelevantLessonTimeReturned() {
		LessonTime expected = new LessonTime(1, 14, LocalTime.of(8, 15), LocalTime.of(9, 45));
		given(lessonTimeDao.get(anyLong())).willReturn(expected);

		final LessonTime actual = lessonTimeService.get(anyLong());

		assertEquals(expected, actual);
		verify(lessonTimeDao, times(1)).get(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		int expected = 1;

		lessonTimeService.delete(anyLong());

		verify(lessonTimeDao, times(expected)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoGetMethodCall_whenUpdate_thenEqualCountOfDaoGetMethodCallReturned() {
		int expected = 2;
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(8, 15), LocalTime.of(9, 45));

		lessonTimeService.update(lessonTime);
		lessonTimeService.update(lessonTime);

		verify(lessonTimeDao, times(expected)).update(lessonTime);
	}

	@Test
	void givenExpectedCountOfDaoGetMethodCall_whenCreate_thenEqualOfDaoGetMethodCallReturned() {
		int expected = 1;
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(8, 15), LocalTime.of(9, 45));

		lessonTimeService.create(lessonTime);

		verify(lessonTimeDao, times(expected)).create(lessonTime);
	}
	
	@Test
	void givenExpectedLessonTimeWithWrongStartAndEndTime_whenCreate_thenDontCallOfDaoCreateMethod() {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(9, 15), LocalTime.of(8, 45));

		lessonTimeService.create(lessonTime);

		verify(lessonTimeDao, never()).create(lessonTime);
	}
	
	@Test
	void givenExpectedLessonTimeWithWrongStartAndEndTime_whenUpdate_thenDontCallOfDaoCreateMethod() {
		LessonTime lessonTime = new LessonTime(1, 14, LocalTime.of(9, 15), LocalTime.of(8, 45));

		lessonTimeService.update(lessonTime);

		verify(lessonTimeDao, never()).update(lessonTime);
	}
}
