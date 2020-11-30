package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.model.Audience;

@ExtendWith(MockitoExtension.class)
class AudienceServiceTest {

	@Mock
	private AudienceDao audienceDao;

	@InjectMocks
	private AudienceService audienceService;

	@Spy
	List<Audience> audiences = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		audiences.add(new Audience(5, 14, 3));
	}

	@Test
	void givenExpectedListOfExistsAudiences_whenGetAll_thenRelevantListOfAudiencesReturned() {
		List<Audience> expected = new ArrayList<>();
		expected.add(new Audience(1, 12, 20));
		expected.add(new Audience(2, 10, 23));
		expected.add(new Audience(3, 6, 15));
		given(audienceDao.getAll()).willReturn(expected);

		List<Audience> actual = audienceService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedAudience_whenGet_thenRelevantAudienceReturned() {
		final Audience expected = new Audience(1, 2, 40);
		given(audienceDao.get(anyLong())).willReturn(expected);

		final Audience actual = audienceService.get(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountOfDaodeleteMethodCall_whenDelete_thenEqualOfDaodeleteMethodCallReturned() {
		doNothing().when(audienceDao).delete(anyLong());

		audienceService.delete(anyLong());

		verify(audienceDao, times(1)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoUpdateMethodCall_whenUpdate_thenEqualCountOfDaoUpdateMethodCallReturned() {
		int expected = 2;
		Audience audience = new Audience(5, 14, 3);
		doNothing().when(audienceDao).update(any(Audience.class));

		audienceService.update(audience);
		audienceService.update(audience);

		verify(audienceDao, times(expected)).update(audience);
	}

	@Test
	void givenExpectedCountOfDaoGetMethodCall_whenCreate_EqualOfDaoGetMethodCallReturned() {
		Audience audience = new Audience(5, 14, 3);
		doThrow(new EmptyResultDataAccessException(1)).when(audienceDao).get(5);

		audienceService.create(audiences.get(0));

		verify(audienceDao, times(1)).create(audience);
	}
}
