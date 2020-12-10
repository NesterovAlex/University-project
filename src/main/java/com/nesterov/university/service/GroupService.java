package com.nesterov.university.service;

import java.util.List;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.GroupDao;
import com.nesterov.university.model.Group;

@Component
public class GroupService {

	private GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public void create(Group group) {
		if (isUniqueName(group.getName())) {
			groupDao.create(group);
		}
	}

	public void delete(long id) {
		groupDao.delete(id);
	}

	public Group get(long id) {
		return groupDao.get(id);
	}

	public void update(Group group) {
		if (!isUniqueName(group.getName()) && groupDao.findByName(group.getName()).getName().equals(group.getName())) {
			groupDao.update(group);
		}
	}

	public List<Group> getAll() {
		return groupDao.findAll();
	}

	private boolean isUniqueName(String name) {
		return groupDao.findByName(name).getName() == null;
	}
}
