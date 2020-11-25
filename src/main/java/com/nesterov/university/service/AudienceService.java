package com.nesterov.university.service;

import org.springframework.stereotype.Component;

import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.model.Audience;

@Component
public class AudienceService {

	private AudienceDao audienceDao;

	public AudienceService(AudienceDao audienceDao) {
		this.audienceDao = audienceDao;
	}
	
	public void createAudience(Audience audience) {
		audienceDao.create(audience);
	}
	
	public void deleteAudience(Audience audience) {
		audienceDao.delete(audience.getId());
	}
	
	public void updateAudience(Audience audience) {
		audienceDao.update(audience);
	}
	
	public void findAudience(Audience audience) {
		audienceDao.get(audience.getId());
	}
	
}
