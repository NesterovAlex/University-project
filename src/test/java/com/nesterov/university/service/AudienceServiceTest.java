package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Optional.of;
import static java.util.Optional.empty;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.service.exceptions.NotFoundException;
import com.nesterov.university.service.exceptions.NotUniqueRoomNumberException;
import com.nesterov.university.model.Audience;

@ExtendWith(MockitoExtension.class)
class AudienceServiceTest {

	@Mock
	private AudienceDao audienceDao;

	@InjectMocks
	private AudienceService audienceService;

	@Test
	void givenListOfExistsAudiences_whenGetAll_thenExpectedListOfAudiencesReturned() {
		List<Audience> expected = new ArrayList<>();
		expected.add(new Audience(1, 12, 20));
		expected.add(new Audience(2, 10, 23));
		expected.add(new Audience(3, 6, 15));
		given(audienceDao.findAll()).willReturn(expected);

		List<Audience> actual = audienceService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenAudience_whenGet_thenExpectedAudienceReturned() {
		Audience expected = new Audience(1, 2, 40);
		given(audienceDao.get(expected.getId())).willReturn(of(expected));

		Audience actual = audienceService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenOptionalEmpty_whenDelete_thenNotFoundExceptionThrown() {
		Audience audience = new Audience(4, 7, 30);
		given(audienceDao.get(audience.getId())).willReturn(empty());

		assertThrows(NotFoundException.class, () -> audienceService.delete(audience.getId()));
	}

	@Test
	void givenAudienceId_whenDelete_thenDeleted() {
		Audience audience = new Audience(7, 5, 40);
		given(audienceDao.get(audience.getId())).willReturn(of(audience));

		audienceService.delete(audience.getId());

		verify(audienceDao).delete(audience.getId());
	}

	@Test
	void givenEmptyOptional_whenDelete_thenNotFoundExceptionThrown() {
		Audience audience = new Audience(7, 5, 40);
		given(audienceDao.get(audience.getId())).willReturn(empty());

		assertThrows(NotFoundException.class, () -> audienceService.delete(audience.getId()));
	}

	@Test
	void givenRoomNumberAudience_whenUpdate_thenUpdated() {
		Audience audience = new Audience(3, 7, 24);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(of(audience));

		audienceService.update(audience);

		verify(audienceDao).update(audience);
	}

	@Test
	void givenExistingAudience_whenUpdate_thenUpdated() {
		Audience audience = new Audience(5, 14, 23);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(of(audience));

		audienceService.update(audience);

		verify(audienceDao).update(audience);
	}

	@Test
	void givenNonExistingAudience_whenUpdate_thenNotUpdated() {
		Audience newAudience = new Audience(5, 14, 23);
		when(audienceDao.findByRoomNumber(newAudience.getRoomNumber())).thenReturn(empty());

		assertThrows(NotUniqueRoomNumberException.class, () -> audienceService.update(newAudience));

		verify(audienceDao, never()).update(newAudience);
	}

	@Test
	void givenExistingAudience_whenCreate_thenCreated() {
		Audience audience = new Audience(1, 4, 33);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(of(audience));

		audienceService.create(audience);

		verify(audienceDao).create(audience);
	}

	@Test
	void givenNonExistingAudience_whenCreate_thenNotCreated() {
		Audience newAudience = new Audience(1, 4, 33);
		when(audienceDao.findByRoomNumber(newAudience.getRoomNumber())).thenReturn(empty());

		assertThrows(NotUniqueRoomNumberException.class, () -> audienceService.create(newAudience));
		verify(audienceDao, never()).create(newAudience);
	}

	@Test
	void givenRoomNumberAudience_whenCreate_thenCreated() {
		Audience audience = new Audience(6, 1, 36);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(of(audience));

		audienceService.create(audience);

		verify(audienceDao).create(audience);
	}

	@Test
	void givenAudienceWithNotUniqueRoomNumber_whenCreate_thenNotUniqueRoomNumberExceptionThrown() {
		Audience newAudience = new Audience(6, 1, 36);
		when(audienceDao.findByRoomNumber(newAudience.getRoomNumber())).thenReturn(empty());

		assertThrows(NotUniqueRoomNumberException.class, () -> audienceService.create(newAudience));
	}

	@Test
	void givenIdNonExistingAudience_whenGet_thenNotFoundExceptionThrown() {
		int expectedId = 99;
		when(audienceDao.get(expectedId)).thenReturn(empty());

		assertThrows(NotFoundException.class, () -> audienceService.get(expectedId));
	}
}
