package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.Audience;

@ExtendWith(MockitoExtension.class)
class AudienceServiceTest {

	@Mock
	private AudienceDao audienceDao;

	@InjectMocks
	private AudienceService audienceService;

	@Test
	void givenListOfExistsAudiences_whenGetAll_thenExpectedListOfAudiencesReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		List<Audience> expected = new ArrayList<>();
		expected.add(new Audience(1, 12, 20));
		expected.add(new Audience(2, 10, 23));
		expected.add(new Audience(3, 6, 15));
		given(audienceDao.findAll()).willReturn(expected);

		List<Audience> actual = audienceService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenAudience_whenGet_thenExpectedAudienceReturned() throws EntityNotFoundException, QueryNotExecuteException {
		Audience expected = new Audience(1, 2, 40);
		given(audienceDao.get(expected.getId())).willReturn(expected);

		Audience actual = audienceService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenAudienceId_whenDelete_thenDeleted() throws NotExistException {
		int audienceId = 1;

		audienceService.delete(audienceId);

		verify(audienceDao).delete(audienceId);
	}

	@Test
	void givenRoomNumberAudience_whenUpdate_thenUpdated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		Audience audience = new Audience(3, 7, 24);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(null);

		audienceService.update(audience);

		verify(audienceDao).update(audience);
	}

	@Test
	void givenExistingAudience_whenUpdate_thenUpdated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		Audience audience = new Audience(5, 14, 23);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(audience);

		audienceService.update(audience);

		verify(audienceDao).update(audience);
	}

	@Test
	void givenNonExistingAudience_whenUpdate_thenNotUpdated()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		Audience existingAudience = new Audience(4, 14, 23);
		Audience newAudience = new Audience(5, 14, 23);
		when(audienceDao.findByRoomNumber(newAudience.getRoomNumber())).thenReturn(existingAudience);

		audienceService.update(newAudience);

		verify(audienceDao, never()).update(newAudience);
	}

	@Test
	void givenExistingAudience_whenCreate_thenCreated()
			throws QueryNotExecuteException, EntityNotFoundException, NotCreateException {
		Audience audience = new Audience(1, 4, 33);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(audience);

		audienceService.create(audience);

		verify(audienceDao).create(audience);
	}

	@Test
	void givenNonExistingAudience_whenCreate_thenNotCreated()
			throws QueryNotExecuteException, EntityNotFoundException, NotCreateException {
		Audience expected = new Audience(1, 4, 33);
		Audience actual = new Audience(2, 4, 33);
		try {
			when(audienceDao.findByRoomNumber(expected.getRoomNumber())).thenReturn(actual);
		} catch (EntityNotFoundException | QueryNotExecuteException e) {
			e.printStackTrace();
		}

		audienceService.create(expected);

		try {
			verify(audienceDao, never()).create(expected);
		} catch (NotCreateException e) {
			e.printStackTrace();
		}
	}

	@Test
	void givenRoomNumberAudience_whenCreate_thenCreated()
			throws QueryNotExecuteException, EntityNotFoundException, NotCreateException {
		Audience audience = new Audience(6, 1, 36);
		when(audienceDao.findByRoomNumber(audience.getRoomNumber())).thenReturn(null);

		audienceService.create(audience);

		verify(audienceDao).create(audience);
	}
}
