package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotFoundException;
import com.nesterov.university.dao.exceptions.NotUniqueRoomNumberException;
import com.nesterov.university.model.Audience;

@Component
public class AudienceService {

	private AudienceDao audienceDao;

	public AudienceService(AudienceDao audienceDao) {
		this.audienceDao = audienceDao;
	}

	public void create(Audience audience) {
		if (isUniqueRoomNumber(audience).getId() == audience.getId()) {
			audienceDao.create(audience);
		}
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
		if (isUniqueRoomNumber(audience).getId() == audience.getId()) {
			audienceDao.update(audience);
		}
	}

	public List<Audience> getAll() {
		List<Audience> audiences = audienceDao.findAll();
		if (audiences.isEmpty()) {
			throw new NotFoundEntitiesException("Not found audiences");
		}
		return audiences;
	}

	private Audience isUniqueRoomNumber(Audience audience) {
		Optional<Audience> optionalAudience = audienceDao.findByRoomNumber(audience.getRoomNumber());
		if (!optionalAudience.isPresent()) {
			String message = format("Not unique roomNumber = '%s'", audience.getRoomNumber());
			throw new NotUniqueRoomNumberException(message);
		}
		return optionalAudience.get();
	}
}
