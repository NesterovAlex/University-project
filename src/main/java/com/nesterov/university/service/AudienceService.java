package com.nesterov.university.service;

import java.util.List;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.Audience;

@Component
public class AudienceService {

	private AudienceDao audienceDao;

	public AudienceService(AudienceDao audienceDao) {
		this.audienceDao = audienceDao;
	}

	public void create(Audience audience) throws QueryNotExecuteException, NotCreateException, EntityNotFoundException {
		if (isUniqueRoomNumber(audience)) {
			audienceDao.create(audience);
		}
	}

	public Audience get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		return audienceDao.get(id);
	}

	public void delete(long id) throws NotExistException {
		audienceDao.delete(id);
	}

	public void update(Audience audience) throws NotCreateException, EntityNotFoundException, QueryNotExecuteException {
		if (isUniqueRoomNumber(audience)) {
			audienceDao.update(audience);
		}
	}

	public List<Audience> getAll() throws EntityNotFoundException, QueryNotExecuteException {
		return audienceDao.findAll();
	}

	private boolean isUniqueRoomNumber(Audience audience) throws EntityNotFoundException, QueryNotExecuteException {
		Audience founded = audienceDao.findByRoomNumber(audience.getRoomNumber());
		return founded == null || founded.getId() == audience.getId();
	}
}
