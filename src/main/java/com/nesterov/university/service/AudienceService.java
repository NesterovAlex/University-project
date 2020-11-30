package com.nesterov.university.service;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.model.Audience;

@Component
public class AudienceService {

	private AudienceDao audienceDao;

	public AudienceService(AudienceDao audienceDao) {
		this.audienceDao = audienceDao;
	}

	public void create(Audience audience) {
		if (!existsById(audience.getId()))
			audienceDao.create(audience);
	}

	public Audience get(long id) {
		return audienceDao.get(id);
	}

	public void delete(long id) {
		audienceDao.delete(id);
	}

	public void update(Audience audience) {
		audienceDao.update(audience);
	}
	
	public List<Audience> getAll() {
		return audienceDao.getAll();
	}

	private boolean existsById(long id) {
		boolean exist;
		try {
			audienceDao.get(id);
			exist = true;
		} catch (EmptyResultDataAccessException e) {
			exist = false;
		}
		return exist;
	}
}
