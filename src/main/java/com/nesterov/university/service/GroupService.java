package com.nesterov.university.service;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.GroupDao;
import com.nesterov.university.model.Group;

@Component
public class GroupService {

	GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}
	
	public void create(Group group) {
		if (!existsById(group.getId()))
			groupDao.create(group);
	}

	public void delete(long id) {
		groupDao.delete(id);
	}

	public Group get(long id) {
		return groupDao.get(id);
	}

	public void update(Group group) {
		groupDao.update(group);
	}

	public List<Group> getAll() {
		return groupDao.getAll();
	}

	public List<Group> findByLessonId(long id) {
		return groupDao.findByLessonId(id);
	}

	private boolean existsById(long id) {
		boolean exist;
		try {
			groupDao.get(id);
			exist = true;
		} catch (EmptyResultDataAccessException e) {
			exist = false;
		}
		return exist;
	}
}
