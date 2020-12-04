package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.model.Audience;

@ExtendWith(MockitoExtension.class)
class AudienceServiceTest {

	@Mock
	private AudienceDao audienceDao;

	@InjectMocks
	private AudienceService audienceService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void givenExpectedListOfExistsAudiences_whenGetAll_thenRelevantListOfAudiencesReturned() {
		List<Audience> expected = new ArrayList<>();
		expected.add(new Audience(1, 12, 20));
		expected.add(new Audience(2, 10, 23));
		expected.add(new Audience(3, 6, 15));
		given(audienceDao.findAll()).willReturn(expected);

		List<Audience> actual = audienceService.getAll();

		assertEquals(expected, actual);
		verify(audienceDao, times(1)).findAll();
	}

	@Test
	void givenExpectedAudience_whenGet_thenRelevantAudienceReturned() {
		final Audience expected = new Audience(1, 2, 40);
		given(audienceDao.get(anyLong())).willReturn(expected);

		final Audience actual = audienceService.get(anyLong());

		assertEquals(expected, actual);
		verify(audienceDao, times(1)).get(anyLong());
	}

	@Test
	void givenExpectedCountOfDaodeleteMethodCall_whenDelete_thenEqualOfDaodeleteMethodCallReturned() {
		int expected = 1;

		audienceService.delete(anyLong());

		verify(audienceDao, times(expected)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoUpdateMethodCall_whenUpdate_thenEqualCountOfDaoUpdateMethodCallReturned() {
		int expected = 2;
		Audience audience = new Audience(5, 14, 3);

		audienceService.update(audience);
		audienceService.update(audience);

		verify(audienceDao, times(expected)).update(audience);
	}

	@Test
	void givenNonExistingAudience_whenCreate_thenDontCallDaoCreateMethod() {
		Audience audience = new Audience(5, 14, 3);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(audience);

		audienceService.create(audience);

		verify(audienceDao, never()).create(audience);
	}
}