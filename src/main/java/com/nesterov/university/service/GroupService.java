package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.GroupDao;
import com.nesterov.university.service.exceptions.NotFoundException;
import com.nesterov.university.service.exceptions.NotUniqueNameException;
import com.nesterov.university.model.Group;

@Component
public class GroupService {

	private GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public void create(Group group) {
		checkUniqueName(group);
		groupDao.create(group);

	}

	public void delete(long id) {
		if (!groupDao.get(id).isPresent()) {
			String message = format("Group with id = '%s' not found", id);
			throw new NotFoundException(message);
		}
		groupDao.delete(id);
	}

	public Group get(long id) {
		String message = format("Group with id = '%s' not found", id);
		return groupDao.get(id).orElseThrow(() -> new NotFoundException(message));
	}

	public void update(Group group) {
		checkUniqueName(group);
		groupDao.update(group);

	}

	public List<Group> getAll() {
		List<Group> groups = groupDao.findAll();
		if (groups.isEmpty()) {
			throw new NotFoundException("Not Found groups");
		}
		return groups;
	}

	private void checkUniqueName(Group group) {
		if (!groupDao.findByName(group.getName()).isPresent()) {
			String message = String.format("Not unique groupName = '%s'", group.getName());
			throw new NotUniqueNameException(message);
		}
	}
}
