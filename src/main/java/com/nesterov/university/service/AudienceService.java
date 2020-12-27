package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotPresentEntityException;
import com.nesterov.university.dao.exceptions.NotUniqueRoomNumberException;
import com.nesterov.university.model.Audience;

@Component
public class AudienceService {

	private AudienceDao audienceDao;

	public AudienceService(AudienceDao audienceDao) {
		this.audienceDao = audienceDao;
	}

	public void create(Audience audience) throws NotUniqueRoomNumberException, NotCreateException {
		if (isUniqueRoomNumber(audience)) {
			audienceDao.create(audience);
		}
	}

	public Audience get(long id) throws NotPresentEntityException {
		String message = format("Audience with id = '%s' not found", id);
		return audienceDao.get(id).orElseThrow(() -> new NotPresentEntityException(message));
	}

	public void delete(long id) throws NotDeleteException {
		audienceDao.delete(id);
	}

	public void update(Audience audience) throws NotUniqueRoomNumberException {
		if (isUniqueRoomNumber(audience)) {
			audienceDao.update(audience);
		}
	}

	public List<Audience> getAll() throws NotFoundEntitiesException {
		List<Audience> audiences = audienceDao.findAll();
		if (audiences.isEmpty()) {
			throw new NotFoundEntitiesException("Not found audiences");
		}
		return audiences;
	}

	private boolean isUniqueRoomNumber(Audience audience) throws NotUniqueRoomNumberException {
		Optional<Audience> optionalAudience = audienceDao.findByRoomNumber(audience.getRoomNumber());
		if (optionalAudience.isPresent() && optionalAudience.orElse(new Audience()).getId() != audience.getId()) {
			String message = format("Not unique roomNumber = '%s'", audience.getRoomNumber());
			throw new NotUniqueRoomNumberException(message);
		}
		return !optionalAudience.isPresent() || optionalAudience.orElse(new Audience()).getId() == audience.getId();
	}
}
