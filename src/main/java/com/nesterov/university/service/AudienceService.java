package com.nesterov.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.model.Audience;

@Component
public class AudienceService {

	private static final Logger log = LoggerFactory.getLogger(AudienceService.class);

	private AudienceDao audienceDao;

	public AudienceService(AudienceDao audienceDao) {
		this.audienceDao = audienceDao;
	}

	public void create(Audience audience) {
		if (isUniqueRoomNumber(audience)) {
			try {
				audienceDao.create(audience);
			} catch (NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public Audience get(long id) {
		Audience audience = null;
		try {
			audience = audienceDao.get(id);
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return audience;
	}

	public void delete(long id) {
		try {
			audienceDao.delete(id);
		} catch (NotExistException e) {
			log.error(e.toString());
		}
	}

	public void update(Audience audience) {
		if (isUniqueRoomNumber(audience)) {
			try {
				audienceDao.update(audience);
			} catch (NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public List<Audience> getAll() {
		List<Audience> audiences = null;
		try {
			audiences = audienceDao.findAll();
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return audiences;
	}

	private boolean isUniqueRoomNumber(Audience audience) {
		Audience founded = null;
		try {
			founded = audienceDao.findByRoomNumber(audience.getRoomNumber());
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return founded == null || founded.getId() == audience.getId();
	}
}
