package com.nesterov.university.service;

import java.util.List;
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
		if (isUniqueRoomNumber(audience)) {
			audienceDao.create(audience);
		}
	}

	public Audience get(long id) {
		return audienceDao.get(id);
	}

	public void delete(long id) {
		audienceDao.delete(id);
	}

	public void update(Audience audience) {
		if (isUniqueRoomNumber(audience)) {
			audienceDao.update(audience);
		}
	}

	public List<Audience> getAll() {
		return audienceDao.findAll();
	}

	private boolean isUniqueRoomNumber(Audience audience) {
		Audience founded = audienceDao.findByRoomNumber(audience.getRoomNumber());
		return founded == null || founded.getId() == audience.getId();
	}
}
