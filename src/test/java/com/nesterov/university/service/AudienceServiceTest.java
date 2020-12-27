package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Optional.ofNullable;
import static java.util.Optional.empty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotPresentEntityException;
import com.nesterov.university.dao.exceptions.NotUniqueRoomNumberException;
import com.nesterov.university.model.Audience;

@ExtendWith(MockitoExtension.class)
class AudienceServiceTest {

	@Mock
	private AudienceDao audienceDao;

	@InjectMocks
	private AudienceService audienceService;

	@Test
	void givenListOfExistsAudiences_whenGetAll_thenExpectedListOfAudiencesReturned() throws NotFoundEntitiesException {
		List<Audience> expected = new ArrayList<>();
		expected.add(new Audience(1, 12, 20));
		expected.add(new Audience(2, 10, 23));
		expected.add(new Audience(3, 6, 15));
		given(audienceDao.findAll()).willReturn(expected);

		List<Audience> actual = audienceService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenAudience_whenGet_thenExpectedAudienceReturned() throws NotPresentEntityException {
		Audience expected = new Audience(1, 2, 40);
		given(audienceDao.get(expected.getId())).willReturn(ofNullable(expected));

		Audience actual = audienceService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenAudienceId_whenDelete_thenDeleted() throws NotDeleteException {
		int audienceId = 1;

		audienceService.delete(audienceId);

		verify(audienceDao).delete(audienceId);
	}

	@Test
	void givenRoomNumberAudience_whenUpdate_thenUpdated() throws NotUniqueRoomNumberException {
		Audience audience = new Audience(3, 7, 24);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(ofNullable(audience));

		audienceService.update(audience);

		verify(audienceDao).update(audience);
	}

	@Test
	void givenExistingAudience_whenUpdate_thenUpdated() throws NotUniqueRoomNumberException {
		Audience audience = new Audience(5, 14, 23);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(ofNullable(audience));

		audienceService.update(audience);

		verify(audienceDao).update(audience);
	}

	@Test
	void givenNonExistingAudience_whenUpdate_thenNotUpdated()
			throws EntityNotFoundException, NotCreateException, NotUniqueRoomNumberException {
		Audience existingAudience = new Audience(4, 14, 23);
		Audience newAudience = new Audience(5, 14, 23);
		when(audienceDao.findByRoomNumber(newAudience.getRoomNumber()))
				.thenReturn(Optional.ofNullable(existingAudience));

		assertThrows(NotUniqueRoomNumberException.class, () -> audienceService.update(newAudience));

		verify(audienceDao, never()).update(newAudience);
	}

	@Test
	void givenExistingAudience_whenCreate_thenCreated() throws NotUniqueRoomNumberException, NotCreateException {
		Audience audience = new Audience(1, 4, 33);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(ofNullable(audience));

		audienceService.create(audience);

		verify(audienceDao).create(audience);
	}

	@Test
	void givenNonExistingAudience_whenCreate_thenNotCreated() throws NotUniqueRoomNumberException, NotCreateException {
		Audience newAudience = new Audience(1, 4, 33);
		Audience existingAudience = new Audience(2, 4, 33);
		when(audienceDao.findByRoomNumber(newAudience.getRoomNumber())).thenReturn(ofNullable(existingAudience));

		assertThrows(NotUniqueRoomNumberException.class, () -> audienceService.create(newAudience));
		verify(audienceDao, never()).create(newAudience);
	}

	@Test
	void givenRoomNumberAudience_whenCreate_thenCreated() throws NotUniqueRoomNumberException, NotCreateException {
		Audience audience = new Audience(6, 1, 36);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(empty());

		audienceService.create(audience);

		verify(audienceDao).create(audience);
	}

	@Test
	void givenNotUniqueRoomNumberAudience_whenCreate_thenNotUniqueRoomNumberExceptionThrown() {
		Audience newAudience = new Audience(6, 1, 36);
		Audience existingAudience = new Audience(7, 1, 36);
		when(audienceDao.findByRoomNumber(newAudience.getRoomNumber())).thenReturn(ofNullable(existingAudience));

		assertThrows(NotUniqueRoomNumberException.class, () -> audienceService.create(newAudience));
	}

	@Test
	void givenEmptyList_whenFindAll_thenNotFoundEntitiesExceptionThrown() {
		when(audienceDao.findAll()).thenReturn(new ArrayList<>());

		assertThrows(NotFoundEntitiesException.class, () -> audienceService.getAll());
	}

	@Test
	void givenIdNonExistingAudience_whenGet_thenNotPresentEntityExceptionThrown() {
		int expectedId = 99;
		when(audienceDao.get(expectedId)).thenReturn(empty());

		assertThrows(NotPresentEntityException.class, () -> audienceService.get(expectedId));
	}
}
