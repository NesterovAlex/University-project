package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.service.exceptions.NotFoundException;
import com.nesterov.university.service.exceptions.NotUniqueRoomNumberException;
import com.nesterov.university.model.Audience;

@Component
public class AudienceService {

	private AudienceDao audienceDao;

	public AudienceService(AudienceDao audienceDao) {
		this.audienceDao = audienceDao;
	}

	public void create(Audience audience) {
		verifyRoomNumberIsUnique(audience);
		audienceDao.create(audience);
	}

	public Audience get(long id) {
		String message = format("Audience with id = '%s' not found", id);
		return audienceDao.get(id).orElseThrow(() -> new NotFoundException(message));
	}

	public void delete(long id) {
		if (!audienceDao.get(id).isPresent()) {
			String message = format("Audience with id = '%s' not found", id);
			throw new NotFoundException(message);
		}
		audienceDao.delete(id);
	}

	public void update(Audience audience) {
		verifyRoomNumberIsUnique(audience);
		audienceDao.update(audience);

	}

	public List<Audience> getAll() {
		List<Audience> audiences = audienceDao.findAll();
		if (audiences.isEmpty()) {
			throw new NotFoundException("Not found audiences");
		}
		return audiences;
	}

	private void verifyRoomNumberIsUnique(Audience audience) {
		if (!audienceDao.findByRoomNumber(audience.getRoomNumber()).isPresent()) {
			String message = format("Not unique roomNumber = '%s'", audience.getRoomNumber());
			throw new NotUniqueRoomNumberException(message);
		}
	}
}
