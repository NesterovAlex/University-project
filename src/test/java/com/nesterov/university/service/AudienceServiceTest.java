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
		verify(audienceDao).findAll();
	}

	@Test
	void givenExpectedAudience_whenGet_thenRelevantAudienceReturned() {
		Audience expected = new Audience(1, 2, 40);
		given(audienceDao.get(expected.getId())).willReturn(expected);

		Audience actual = audienceService.get(expected.getId());

		assertEquals(expected, actual);
		verify(audienceDao).get(expected.getId());
	}

	@Test
	void givenExpectedId_whenDelete_thenDeleted() {
		int expected = 1;

		audienceService.delete(expected);

		verify(audienceDao).delete(expected);
	}

	@Test
	void givenExpectedRoomNumberOfExistingAudience_whenUpdate_thenUpdated() {
		Audience audience = new Audience(3, 7, 24);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(null);

		audienceService.update(audience);

		verify(audienceDao).update(audience);
	}

	@Test
	void givenExistingAudience_whenUpdate_thenUpdated() {
		Audience audience = new Audience(5, 14, 23);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(audience);

		audienceService.update(audience);

		verify(audienceDao).update(audience);
	}

	@Test
	void givenNonExistingAudience_whenUpdate_thenNotUpdated() {
		Audience existingAudience = new Audience(4, 14, 23);
		Audience newAudience = new Audience(5, 14, 23);
		when(audienceDao.findByRoomNumber(newAudience.getRoomNumber())).thenReturn(existingAudience);

		audienceService.update(newAudience);

		verify(audienceDao, never()).update(newAudience);
	}

	@Test
	void givenExistingAudience_whenCreate_thenExpectedCountOfDaoCreateMethodCallReturned() {
		int expected = 1;
		Audience audience = new Audience(1, 4, 33);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(audience);

		audienceService.create(audience);

		verify(audienceDao, times(expected)).create(audience);
	}

	@Test
	void givenNonExistingAudience_whenCreate_thenDontCallDaoCreateMethod() {
		Audience expected = new Audience(1, 4, 33);
		Audience actual = new Audience(2, 4, 33);
		when(audienceDao.findByRoomNumber(expected.getRoomNumber())).thenReturn(actual);

		audienceService.create(expected);

		verify(audienceDao, never()).create(expected);
	}

	@Test
	void givenExpectedRoomNumberExistingAudience_whenCreate_thenCreated() {
		Audience audience = new Audience(6, 1, 36);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(null);

		audienceService.create(audience);

		verify(audienceDao).create(audience);
	}
}
