package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import com.nesterov.university.dao.LessonTimeDao;
import com.nesterov.university.model.LessonTime;

@ExtendWith(MockitoExtension.class)
class LessonTimeServiceTest {

	@Mock
	private LessonTimeDao lessonTimeDao;

	@InjectMocks
	private LessonTimeService lessonTimeService;

	@Spy
	List<LessonTime> lessonTimes = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		lessonTimes.add(new LessonTime(1, 14, LocalTime.of(8, 15), LocalTime.of(9, 45)));
	}

	@Test
	void givenExpectedListOfExistsLessonTimes_whenGetAll_thenRelevantListOfLessonTimesReturned() {
		List<LessonTime> expected = new ArrayList<>();
		expected.add(lessonTimes.get(0));
		given(lessonTimeDao.getAll()).willReturn(expected);

		List<LessonTime> actual = lessonTimeService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedLessonTime_whenGet_thenRelevantLessonTimeReturned() {
		final LessonTime expected = lessonTimes.get(0);
		given(lessonTimeDao.get(anyLong())).willReturn(expected);

		final LessonTime actual = lessonTimeService.get(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		doNothing().when(lessonTimeDao).delete(anyLong());

		lessonTimeService.delete(anyLong());

		verify(lessonTimeDao, times(1)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoGetMethodCall_whenUpdate_thenEqualCountOfDaoGetMethodCallReturned() {
		int expected = 2;
		LessonTime lessonTime = lessonTimes.get(0);
		doNothing().when(lessonTimeDao).update(any(LessonTime.class));

		lessonTimeService.update(lessonTime);
		lessonTimeService.update(lessonTime);

		verify(lessonTimeDao, times(expected)).update(lessonTime);
	}

	@Test
	void givenExpectedCountOfDaoGetMethodCall_whenCreate_EqualOfDaoGetMethodCallReturned() {
		LessonTime lessonTime = lessonTimes.get(0);
		doThrow(new EmptyResultDataAccessException(1)).when(lessonTimeDao).get(lessonTime.getId());

		lessonTimeService.create(lessonTimes.get(0));

		verify(lessonTimeDao, times(1)).create(lessonTime);
	}
}
