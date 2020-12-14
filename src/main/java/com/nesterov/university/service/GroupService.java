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
		if (isUniqueName(group)) {
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
		if (isUniqueName(group)) {
			groupDao.update(group);
		}
	}

	public List<Group> getAll() {
		return groupDao.findAll();
	}

	private boolean isUniqueName(Group group) {
		Group founded = groupDao.findByName(group.getName());
		return founded == null || founded.getId() == group.getId();
	}
}
